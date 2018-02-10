package publicGUI.gameJPanel.flappybird;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

//为了防止阻塞UI线程，创建下面这个专门播放声音的线程
public class PlaySounds extends Thread {
	// 播放器
	private AudioClip[] player;
	// 是否播放声音
	private int isPlay = 1;

	public PlaySounds() {
		player = new AudioClip[3];
		try {
			player[0] = Applet.newAudioClip(new URL("file:gameResource/flappyBirdRes/Wing.wav"));
			player[1] = Applet.newAudioClip(new URL("file:gameResource/flappyBirdRes/Point.wav"));
			player[2] = Applet.newAudioClip(new URL("file:gameResource/flappyBirdRes/Hit.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// 重置播放声音标志
	public void setIsPlay(int i) {
		isPlay = i;
	}

	public int getIsPlay() {
		return isPlay;
	}

	// 播放声音
	public void run() {
		while (FlappyBirdGameUI.stopThread==0) {
			System.out.println("1");
			if (isPlay == 1 && FlappyBirdGameUI.flag == 1) {
				player[0].play();
				isPlay = 0;
			} else if (isPlay == 2 && FlappyBirdGameUI.flag == 1) {
				player[1].play();
				isPlay = 0;
			} else if (isPlay == 3 && FlappyBirdGameUI.flag == 2) {
				player[2].play();
				isPlay = 0;
			}
		}
	}
}
