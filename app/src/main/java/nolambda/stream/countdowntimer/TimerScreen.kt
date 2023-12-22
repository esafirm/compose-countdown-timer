package nolambda.stream.countdowntimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.concurrent.TimeUnit

private val TimerTextStyle = TextStyle(
    fontSize = 60.sp,
    fontWeight = FontWeight.W300,
)

private val CoolGreen = Color(0XFF65B788)

@Composable
fun TimerScreen(timerViewModel: TimerViewModel = viewModel()) {
    val timerState by timerViewModel.timerState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        val time by timerViewModel.time.collectAsState()

        CircularProgressIndicator(
            progress = time.toFloat() / TimeUnit.MINUTES.toMillis(1),
            color = CoolGreen,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .height(220.dp)
                .width(220.dp)
        )

        Row {
            Text(text = "${time / 60000}:", style = TimerTextStyle) // minutes
            Text(text = "${time / 1000 % 60}:", style = TimerTextStyle) // seconds
            Text(text = "${time % 1000 / 100}", style = TimerTextStyle) // milliseconds
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = timerViewModel::startOrPause) {
                Text(if (timerState == TimerState.RUNNING) "Pause" else "Start")
            }

            Button(onClick = timerViewModel::stop) {
                Text("Stop")
            }
        }
    }
}
