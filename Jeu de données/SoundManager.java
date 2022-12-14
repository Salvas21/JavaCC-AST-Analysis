import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class SoundManager
{
	private HashMap<String, Sound> sounds;
	private HashMap<String, Music> musics;
	private ArrayList<Sound>	soundsList;
	private ArrayList<Music> musicsList;
	private String currentMusic = "";
	
	private	float musicsVolume = 1;
	private	float soundVolume = 1;
	private float musicsPitch = 1;
	private float soundPitch = 1;
	
	public SoundManager () 
	{
		sounds = new HashMap<String, Sound>();
		musics = new HashMap<String, Music>();
		soundsList = new ArrayList<Sound>();
		musicsList = new ArrayList<Music>();
	}
	
	public String GetCurrentSong()
	{
		return currentMusic;
	}
	
	public void SetMusicsVolume(float volume)
	{
		musicsVolume = volume;
		
		for (int i = 0; i < musicsList.size(); i++)
			musicsList.get(i).setVolume(volume);
	}
	public void SetMusicsPitch(float pitch)
	{
		musicsPitch = pitch;
	}
	public void SetSoundsVolume(float volume)
	{
		soundVolume = volume;
	}
	public void SetSoundsPitch(float pitch)
	{
		soundPitch = pitch;
	}
	
	public void LoadSound(String soundName, String fileName)
	{
		try 
		{
			Sound newSound = new Sound(fileName);
			sounds.put(soundName, newSound);
			soundsList.add(newSound);
		} 
		catch (SlickException e)
		{
			e.printStackTrace();
		}	
	}
	public void LoadMusic(String musicName, String fileName) throws SlickException
	{
		try 
		{
			Music newMusic = new Music(fileName);
			musics.put(musicName, newMusic);
			musicsList.add(newMusic);
		} 
		catch (SlickException e)
		{
			e.printStackTrace();
		}	
	}
	public void PlayMusic(String musicName)
	{
		if (musics.containsKey(musicName))
			musics.get(musicName).play(musicsPitch, musicsVolume);
	}
	public void PlayMusic(String musicName, boolean looped)
	{
		if (musics.containsKey(musicName))
		{
			currentMusic = musicName;
			Music music = musics.get(musicName);
			
			if (looped == true)
				music.loop(musicsPitch, musicsVolume);
			else
				music.play(musicsPitch, musicsVolume);
		}
	}
	public void PlayMusic(String musicName, boolean looped, float pitch, float volume)
	{
		if (musics.containsKey(musicName))
		{
			currentMusic = musicName;
			Music music = musics.get(musicName);
			
			if (looped == true)
				music.loop(pitch, volume);
			else
				music.play(pitch, volume);
		}
	}
	public void PlaySound(String soundName)
	{
		if (sounds.containsKey(soundName))
			sounds.get(soundName).play(soundPitch, soundVolume);
	}
	public void PlaySound(String soundName, boolean looped)
	{
		if (sounds.containsKey(soundName))
		{
			Sound sound = sounds.get(soundName);
			
			if (looped == true)
				sound.loop(soundPitch, soundVolume);
			else
				sound.play(soundPitch, soundVolume);
		}
	}
	public void PlaySound(String soundName, boolean looped, float pitch, float volume)
	{
		if (sounds.containsKey(soundName))
		{
			Sound sound = sounds.get(soundName);
			
			if (looped == true)
				sound.loop(pitch, volume);
			else
				sound.play(pitch, volume);
		}
	}
	public void StopAllSound()
	{
		for (int i = 0; i < soundsList.size(); i++)
			soundsList.get(i).stop();
	}
	public void StopAllMusic()
	{
		for (int i = 0; i < musicsList.size(); i++)
			musicsList.get(i).stop();
	}
}
