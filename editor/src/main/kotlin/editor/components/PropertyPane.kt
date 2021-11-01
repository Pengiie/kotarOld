package editor.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.pengie.kotaro.Application
import dev.pengie.kotaro.scene.components.Camera
import dev.pengie.kotaro.scene.components.Tag
import dev.pengie.kotaro.scene.components.Transform
import editor.EditorState
import kotlin.reflect.KMutableProperty0

object PropertyPane {
    var newEntity = false
}
@Composable
fun PropertyPane() = Column(modifier = Modifier.fillMaxSize()) {
    Surface(modifier = Modifier.padding(start = 10.dp), elevation = 1.dp, shape = RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp)) {
        Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), text = "Properties")
    }
    Surface(modifier = Modifier.fillMaxSize(), elevation = 2.dp) {
        if(EditorState.selectedEntity != null) {
            Column {
                if (EditorState.selectedEntity == -1) {

                } else {
                    val id = EditorState.selectedEntity!!
                    if (Application.scene.hasComponent<Tag>(id))
                        TagProperties(Application.scene.getComponent<Tag>(id)!!)
                    if (Application.scene.hasComponent<Transform>(id))
                        TransformProperties(Application.scene.getComponent<Transform>(id)!!)
                    if (Application.scene.hasComponent<Camera>(id))
                        CameraProperties(Application.scene.getComponent<Camera>(id)!!)
                    PropertyPane.newEntity = false
                }
            }
        }
    }
}

private const val textSize = 15
@Composable
fun ComponentProperties(name: String, content: @Composable ColumnScope.() -> Unit) {
    with(LocalDensity.current) {
        Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight().height(textSize.sp.toDp() * 1.5f), elevation = 3.dp) {
            Box(modifier = Modifier.padding(start = 5.dp)) {
                Text(modifier = Modifier.align(Alignment.CenterStart), text = name, fontSize = textSize.sp)
            }
        }
        Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 3.dp), elevation = 2.dp) {
            Column(modifier = Modifier.padding(3.dp), content = content)
        }
    }
}

@Composable
fun TagProperties(tag: Tag) = ComponentProperties("Tag") {
    var hack by remember { mutableStateOf(false)}
    if(hack)
        ;
    Row {
       Text(text = "Name:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))
       EditorTextField(modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth(), value = tag.name, onValueChange = {
           tag.name = it
           hack = !hack
       })
    }
}

@Composable
fun TransformProperties(transform: Transform) = ComponentProperties("Transform") {
    Row(modifier = Modifier.padding(bottom = 3.dp)) {
        Text(text = "Position:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))

        FloatField(transform.position::x, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.position::y, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.position::z, Modifier.align(Alignment.CenterVertically).width(65.dp))
    }

    Row(modifier = Modifier.padding(bottom = 3.dp)) {
        Text(text = "Rotation:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))

        FloatField(transform.eulerAngles::x, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.eulerAngles::y, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.eulerAngles::z, Modifier.align(Alignment.CenterVertically).width(65.dp))
    }

    Row {
        Text(text = "Scale:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))

        FloatField(transform.scale::x, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.scale::y, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
        FloatField(transform.scale::z, Modifier.align(Alignment.CenterVertically).width(65.dp))
    }
}

@Composable
fun CameraProperties(camera: Camera) = ComponentProperties("Camera") {
    Row(modifier = Modifier.padding(bottom = 3.dp)) {
        Text(text = "Near Plane:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))
        FloatField(camera::nearPlane, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
    }
    Row(modifier = Modifier.padding(bottom = 3.dp)) {
        Text(text = "Far Plane:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))
        FloatField(camera::farPlane, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
    }
    Row(modifier = Modifier.padding(bottom = 3.dp)) {
        Text(text = "FOV:", fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 5.dp))
        FloatField(camera::fov, Modifier.align(Alignment.CenterVertically).width(65.dp).padding(end = 4.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FloatField(property: KMutableProperty0<Float>, modifier: Modifier = Modifier, onChange: (Float) -> Unit = {
    property.set(it)
}) {
    var text by remember { mutableStateOf(property.get().toString()) }
    val focusManager = LocalFocusManager.current
    if(PropertyPane.newEntity)
        text = "%.2f".format(property.get())
    EditorTextField(value = text, modifier = modifier.
    onFocusEvent {
         if(!it.isFocused)
             onChange.invoke(text.toFloat())
    }.onKeyEvent {
        if(it.nativeKeyEvent.keyCode == java.awt.event.KeyEvent.VK_ENTER) {
            focusManager.clearFocus(true)
            text = "%.2f".format(property.get())
            return@onKeyEvent true
        }
        false
    }, onValueChange = {
        text = it.replace(Regex("[^0-9.-]"), "")
    })
}


