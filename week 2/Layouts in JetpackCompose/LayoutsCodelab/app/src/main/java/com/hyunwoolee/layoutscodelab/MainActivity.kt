package com.hyunwoolee.layoutscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.hyunwoolee.layoutscodelab.ui.theme.LayoutsCodelabTheme
import kotlinx.coroutines.launch

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ImageList()
                }
            }
        }
    }
}

/*
* Most composables accept an optional modifier parameter to make them more flexible
* If you're creating your own composable, consider having a modifier as a parameter,
* default it to Modifier
*
* By convention, the modifier is specified as the first optional parameter of a function.
* This enables you to specify a modifier on a composable without having to name all parameters.
* */
@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {

        }
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(text = "HyunWoo Lee", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "Nunu Lee", style = MaterialTheme.typography.body2)
            }
        }
    }
}


@Composable
fun ScaffoldSample() {
    Scaffold(
        // TopAppBar 역시 Slot Api로 구성됨
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutCodelab")
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Hi There")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Composable
fun SimpleList() {
    // We save the scrolling position with this state that can also
    // be used to programmatically scroll the list
    val scrollState = rememberScrollState()

    // 이런 방식으로 만들 시 한번에 모든 아이템들을 전부 렌더링한다 -> 비효율적
    // Lazy Column으로 리스트를 만들어보자
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(text = "Item $it")
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ), contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item $index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun LazyListSample() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            Text(text = "Lazy Column's Item $it")
        }
    }
}

@Composable
fun ImageList() {
    val scrollState = rememberLazyListState()
    val listSize = 100
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scroll to the top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scroll to the end")
            }
        }
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
            items(100) {
                ImageListItem(index = it)
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (buttonLeft, buttonRight, text) = createRefs()
        val barrier = createEndBarrier(buttonLeft)
        Button(onClick = { /*TODO*/ },
            // Assign reference "button" to the Button Composable
            // and constrain it to the top of the ConstraintLayout
            modifier = Modifier.constrainAs(buttonLeft) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(buttonRight.start)
            }
        ) {
            Text(text = "Button 1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(buttonLeft.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
        })

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.constrainAs(buttonRight) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(buttonLeft.end)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "Button 2")
        }
    }
}

//@Preview("ListSample", showBackground = true, showSystemUi = true)
//@Composable
//fun ListPreview() {
//    LayoutsCodelabTheme {
//        ImageList()
//    }
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutsCodelabTheme {
        ScaffoldSample()
    }
}

@Preview(name = "PhotographerCardPreview")
@Composable
fun PhotographerCardPreview() {
    PhotographerCard()
}

@Preview(name = "CustomLayoutModifierSample")
@Composable
fun CustomLayoutModifierSample() {
    LayoutsCodelabTheme {
        Column {
            Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
            Text("Hi there!", Modifier.padding(top = 32.dp))
        }
    }
}

@Preview(name = "CustomLayoutSample")
@Composable
fun CustomLayoutSample() {
    LayoutsCodelabTheme {
//        CustomLayout(Modifier.padding(8.dp)) {
//            Text("MyOwnColumn")
//            Text("places items")
//            Text("vertically.")
//            Text("We've done it by hand!")
//        }
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            StaggeredGrid {
                topics.map { Chip(text = it, modifier = Modifier.padding(8.dp)) }
            }
        }
    }
}

@Preview("ConstraintLayoutSample", showBackground = true)
@Composable
fun ConstraintLayoutSample() {
    LayoutsCodelabTheme {
        ConstraintLayoutContent()
    }
}