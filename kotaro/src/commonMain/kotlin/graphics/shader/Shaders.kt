package dev.pengie.kotaro.graphics.shader

import dev.pengie.kotaro.graphics.RenderSystem
import dev.pengie.kotaro.graphics.shader.builder.shader
import dev.pengie.kotaro.graphics.shader.builder.ShaderVariablePrimitiveType.*

object Shaders {
    val Main = shader {
        vertexShader {
            val position = input(0, "position", VEC3)
            val textureCoord = input(1, "textureCoord", VEC2)
            val normal = input(2, "normal", VEC3)

            val passTextureCoord = output("passTextureCoord", VEC2)
            val passNormal = output("passNormal", VEC3)
            val fragPos = output("fragPos", VEC3)

            val projViewMatrix = uniform("projViewMat", MAT4)
            val model = uniform("modelMat", MAT4)

            main {
                position { projViewMatrix * model * vec4(position, 1.0f.exp) }
                passTextureCoord.equal { textureCoord }
                passNormal.equal { normal }
                fragPos.equal { vec3(model * vec4(position, 1.0f.exp)) }
            }
        }
        fragmentShader {
            val textureCoord = input("passTextureCoord", VEC2)
            val passNormal = input("passNormal", VEC3)
            val fragPos = input("fragPos", VEC3)

            val outColor = output("outColor", VEC4)

            val ambientLightStruct = struct("AmbientLight") {
                member("color", VEC3)
                member("strength", FLOAT)
            }
            val ambientLights = uniform("ambientLights", ambientLightStruct.type, RenderSystem.LIGHT_AMBIENT_COUNT)

            val pointLightStruct = struct("PointLight") {
                member("color", VEC3)
                member("position", VEC3)
                member("strength", FLOAT)
            }
            val pointLights = uniform("pointLights", pointLightStruct.type, RenderSystem.LIGHT_POINT_COUNT)

            val directionalLightStruct = struct("DirectionalLight") {
                member("color", VEC3)
                member("direction", VEC3)
                member("strength", FLOAT)
            }
            val directionalLights = uniform("directionalLights", directionalLightStruct.type, RenderSystem.LIGHT_DIRECTIONAL_COUNT)

            val materialStruct = struct("Material") {
                member("color", VEC4)
                member("hasTexture", BOOL)
                member("texture", SAMPLER2D)
                member("doLighting", BOOL)
            }
            val material = uniform("material", materialStruct.type)

            val calculatePointLight = function("calculatePointLight", VEC3) {
                val light = parameter("pointLight", pointLightStruct.type)
                val normal = parameter("normal", VEC3)

                val lightToFrag = variable("lightToFrag", VEC3) { light["position"] - fragPos }
                val direction = variable("direction", VEC3) { normalize(lightToFrag) }

                val distance = variable("distance", FLOAT) { length(lightToFrag) }
                val attenuation = variable("attenuation", FLOAT) { 1.0f.exp / paren(1.0f.exp + paren(distance * distance)) }
                attenuation.equal { attenuation * light["strength"] }

                val diffuse = variable("diffuse", VEC3) { max(dot(normal, direction), 0.0f.exp) * light["color"] }
                return@function expression { diffuse * attenuation }
            }

            val calculateDirectionalLight = function("calculateDirectionalLight", VEC3) {
                val light = parameter("directionalLight", directionalLightStruct.type)
                val normal = parameter("normal", VEC3)
                return@function expression { max(dot(normal, normalize(light["direction"])), 0.0f.exp) * light["strength"] * light["color"] }
            }

            main {
                val normal = variable("normal", VEC3) { normalize(passNormal) }
                val shading = variable("shading", VEC3) { vec3(0.0f.exp) }

                forI("i", RenderSystem.LIGHT_AMBIENT_COUNT) {
                    shading.equal { shading + ambientLights[i]["color"] * ambientLights[i]["strength"] }
                }
                forI("i", RenderSystem.LIGHT_POINT_COUNT) {
                    shading.equal { shading + call(calculatePointLight, pointLights[i], normal) }
                }
                forI("i", RenderSystem.LIGHT_DIRECTIONAL_COUNT) {
                    shading.equal { shading + call(calculateDirectionalLight, directionalLights[i], normal) }
                }

                val mat = variable("mat", VEC4) { material["color"] }
                runIf(material["hasTexture"]) {
                    mat.equal { mat * texture(material["texture"], textureCoord) }
                }
                runIf(expression { !material["doLighting"] }) {
                    shading.equal { vec3(1.0f.exp) }
                }
                val result = variable("result", VEC4) { mat * vec4(shading, 1.0f.exp) }
                outColor.equal { result }
            }
        }
    }

    val Screen = shader {
        vertexShader {
            val position = input(0, "position", VEC3)
            val textureCoord = input(1, "textureCoord", VEC2)

            val passTextureCoord = output("passTextureCoord", VEC2)

            main {
                passTextureCoord.equal { textureCoord }
                position { vec4(position, 1.0f.exp) }
            }
        }
        fragmentShader {
            val textureCoord = input("passTextureCoord", VEC2)

            val outColor = output("outColor", VEC4)

            val texture = uniform("texture", SAMPLER2D)

            main {
                outColor.equal { texture(texture, textureCoord) }
            }
        }
    }
}