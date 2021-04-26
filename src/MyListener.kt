import com.intellij.notification.Notification
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl


class MyListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.messageBus.connect().subscribe(
                Notifications.TOPIC,
                object : Notifications {
                    override fun notify(notification: Notification) {
                        if ("Gradle build failed" in notification.content) {
                            playSound("sound.wav")
                        }
                    }
                })
    }

    @Synchronized
    fun playSound(url: String) {
        Thread {
            try {
                val clip = AudioSystem.getClip()
                val inputStream: AudioInputStream = AudioSystem.getAudioInputStream(
                        MyListener::class.java.getResourceAsStream(url)
                )
                clip.open(inputStream)
                play(clip)
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }.start()
    }

    fun play(clip: Clip) {
        val control = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        control.value = -15f
        clip.start()
    }
}