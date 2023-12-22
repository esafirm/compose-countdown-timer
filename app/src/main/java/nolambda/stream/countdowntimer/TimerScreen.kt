package nolambda.stream.countdowntimer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

private val TimerTextStyle = TextStyle(
    fontSize = 60.sp,
    fontWeight = FontWeight.W300,
)

private val CoolGreen = Color(0XFF65B788)

@Composable
fun TimerScreen(timerViewModel: TimerViewModel = koinViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        val time by timerViewModel.timeText.collectAsState()
        val progress by timerViewModel.indicatorProgress.collectAsState()

        CircleIndicator(progress = progress)

        Text(text = time, style = TimerTextStyle)

        val timerState by timerViewModel.timerState.collectAsState()

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

@Composable
private fun CircleIndicator(progress: Float) {
    CircularProgressIndicator(
        progress = progress,
        color = CoolGreen,
        strokeWidth = 10.dp,
        strokeCap = StrokeCap.Round,
        modifier = Modifier.size(260.dp)
    )
}
