package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.net.URL;

public class ResourceLoader {
	public URL loadResource(String filepath){
		URL url = getClass().getResource(filepath);
		return url;
	}
}
