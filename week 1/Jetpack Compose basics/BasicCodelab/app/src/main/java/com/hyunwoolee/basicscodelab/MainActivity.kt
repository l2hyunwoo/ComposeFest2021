package com.hyunwoolee.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyunwoolee.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp()
            }
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = 4.dp
    ) {
        CardContent(name)
    }
}

@Composable
fun CardContent(name: String) {
    // Recomposition이 일어날 때, remember가 붙은 변수는 Recomposition 대상에서 제외된다
    var expanded by remember { mutableStateOf(false) }
    // Surface를 MaterialTheme으로 지정했는데 글자 색이 하얀색으로 바뀜
    /*
    * In this case, Surface understands that, when the background is set to the primary color,
    * any text on top of it should use the onPrimary color, which is also defined in the theme.
    * You can learn more about this in the Theming your app section.
    * */
    Row(
        modifier = Modifier.padding(24.dp).animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Hello")
            Text(
                text = name, style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4)
                )
            }
        }
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Image(
                painter = rememberVectorPainter(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore),
                contentDescription = if (expanded) stringResource(R.string.show_less)
                else stringResource(R.string.show_more)
            )
        }
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
//    Column(modifier = Modifier.padding(vertical = 4.dp)) {
//        names.forEach { Greeting(it) }
//    }
    /*
    * LazyColumn doesn't recycle its children like RecyclerView.
    * It emits new Composables as you scroll through it and is still performant,
    * as emitting Composables is relatively cheap compared to instantiating Android Views.
    * */
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(names) { name -> Greeting(name) }
    }
}

@Composable
private fun OnboardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
private fun MyApp() {
    /*
    * Configuration Change가 일어나도 State가 초기화되지 않게(ViewModel property와 비슷하넹)
    * */
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen { shouldShowOnboarding = false }
    } else {
        Greetings()
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    BasicsCodelabTheme {
        MyApp()
    }
}