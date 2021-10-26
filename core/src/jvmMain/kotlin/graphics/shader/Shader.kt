package dev.pengie.kotaro.graphics.shader

import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.math.Matrix4f
import org.lwjgl.opengl.GL20.*
import dev.pengie.kotaro.graphics.RenderSystem
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.graphics.shader.builder.ShaderBuilder
import dev.pengie.kotaro.graphics.shader.builder.toSource
import dev.pengie.kotaro.logging.logInfo
import dev.pengie.kotaro.math.Vector3f

const val MAIN_VERTEX = """
    #version 400 core
    layout(location = 0) in vec3 position;
    layout(location = 1) in vec2 textureCoord;
    layout(location = 2) in vec3 normal;
    
    out vec2 passTextureCoord;
    out vec3 passNormal;
    out vec3 fragPos;
    
    uniform mat4 ${Uniforms.PROJ_VIEW_MATRIX};
    uniform mat4 ${Uniforms.MODEL_MATRIX};
    void main() {
      gl_Position = ${Uniforms.PROJ_VIEW_MATRIX} * ${Uniforms.MODEL_MATRIX} * vec4(position, 1.0);
      passTextureCoord = textureCoord;
      passNormal = normal;
      fragPos = vec3(${Uniforms.MODEL_MATRIX} * vec4(position, 1.0));
    }
    """

const val MAIN_FRAGMENT = """
    #version 400 core
    in vec2 passTextureCoord;
    in vec3 passNormal;
    in vec3 fragPos;
    out vec4 outColor;
    
    struct AmbientLight 
    {
      vec3 color;
      float strength;
    };
    uniform AmbientLight ambientLights[${RenderSystem.LIGHT_AMBIENT_COUNT}];
    
    struct PointLight 
    {
      vec3 position;
      vec3 color;
      float strength;
    };
    uniform PointLight pointLights[${RenderSystem.LIGHT_POINT_COUNT}];
    
    struct DirectionalLight 
    {
      vec3 direction;
      vec3 color;
      float strength;
    };
    uniform DirectionalLight directionalLights[${RenderSystem.LIGHT_DIRECTIONAL_COUNT}];
    
    struct Material
    {
      vec4 color;
      bool hasTexture;
      sampler2D texture;
      bool doLighting;
    };
    uniform Material material;
    
    vec3 calculatePointLight(PointLight light, vec3 normal)
    {
      vec3 lightToFrag = light.position - fragPos;
      vec3 direction = normalize(lightToFrag);
      
      float distance = length(lightToFrag);
      float attenuation = 1.0 / (1 + (distance * distance));
      attenuation *= light.strength;
      
      vec3 diffuse = max(dot(normal, direction), 0.0) * light.color;
      
      return diffuse * attenuation;
    }
    
    vec3 calculateDirectionalLight(DirectionalLight light, vec3 normal)
    {      
      return max(dot(normal, normalize(light.direction)), 0.0) * light.color * light.strength;
    }
    
    void main() 
    {
      vec3 normal = normalize(passNormal);
    
      vec3 shading = vec3(0.0);
      for(int i = 0; i < ${RenderSystem.LIGHT_AMBIENT_COUNT}; i++)
        shading += ambientLights[i].color * ambientLights[i].strength;
      for(int i = 0; i < ${RenderSystem.LIGHT_POINT_COUNT}; i++)
        shading += calculatePointLight(pointLights[i], normal);
      for(int i = 0; i < ${RenderSystem.LIGHT_DIRECTIONAL_COUNT}; i++)
        shading += calculateDirectionalLight(directionalLights[i], normal);
        
      vec4 mat = material.color;
      if(material.hasTexture)
      {
        mat *= texture(material.texture, passTextureCoord);
      }
      if(!material.doLighting)
        shading = vec3(1.0);
        
      vec4 result = mat * vec4(shading, 1.0) * vec4(1.0, 1.0, 1.0, 1.0);
      if(result.a < 0.1)
        discard;
      outColor = result;
    }
    """

const val SCREEN_VERTEX = """
    #version 400 core
    layout(location = 0) in vec3 position;
    layout(location = 1) in vec2 textureCoord;
    out vec2 passTextureCoord;
    void main() {
      gl_Position = vec4(position, 1.0);
      passTextureCoord = textureCoord;
    }
    """

const val SCREEN_FRAGMENT = """
    #version 400 core
    in vec2 passTextureCoord;
    out vec4 outColor;
    uniform sampler2D screen;
    void main() {
      outColor = texture(screen, passTextureCoord);
    }
    """

class OpenGLShader(builder: ShaderBuilder) : Shader {
    private var program: Int = 0;

    val vertexSource = builder.vertexShader!!.toSource()
    val fragmentSource = builder.fragmentShader!!.toSource()

    override fun init() {
        //logInfo(vertexSource)
        //logInfo(fragmentSource)
        program = glCreateProgram()
        val vertex = compileShader(vertexSource, GL_VERTEX_SHADER)
        val fragment = compileShader(fragmentSource, GL_FRAGMENT_SHADER)

        glAttachShader(program, vertex)
        glAttachShader(program, fragment)
        glLinkProgram(program)
        glDetachShader(program, vertex)
        glDetachShader(program, fragment)
        glDeleteShader(vertex)
        glDeleteShader(fragment)
    }

    override fun start() {
        glUseProgram(program)
    }

    override fun stop() {
        glUseProgram(0)
    }

    private fun getUniformLocation(name: String): Int = glGetUniformLocation(program, "kotU_$name")

    override fun uniformBool(location: String, value: Boolean) {
        glUniform1i(getUniformLocation(location), if(value) 1 else 0)
    }

    override fun uniformFloat(location: String, value: Float) {
        glUniform1f(getUniformLocation(location), value)
    }

    override fun uniformVector3f(location: String, vector: Vector3f) {
        glUniform3f(getUniformLocation(location), vector.x, vector.y, vector.z)
    }

    override fun uniformColor(location: String, color: Color) {
        glUniform4f(getUniformLocation(location), color.r / 255f, color.g / 255f, color.b / 255f, color.a / 255f)
    }

    /*override fun uniformVector4f(location: String, vector: Vector4f) {
        glUniform3f(getUniformLocation(location), vector.x, vector.y, vector.z)
    }*/

    override fun uniformMatrix4f(location: String, matrix: Matrix4f) {
        glUniformMatrix4fv(getUniformLocation(location), false, matrix.toArray().toFloatArray())
    }

    override fun uniformTexture(location: String, texture: Texture) {
        glUniform1i(getUniformLocation(location), 0)
        glActiveTexture(0)
        texture.bind()
    }

    private fun compileShader(source: String, type: Int): Int {
        val id = glCreateShader(type)
        glShaderSource(id, source)
        glCompileShader(id)
        if(glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw RuntimeException(glGetShaderInfoLog(id))

        return id
    }
}

actual object ShaderFactory {
    actual fun createShader(builder: ShaderBuilder): Shader = OpenGLShader(builder)
}