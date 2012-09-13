package org.pockys.allingrid;

public class IconInfo {

	private int image;
	private String name;
	private String info;
	private int ID;

	public IconInfo(int _image, String _name, String _info, int _ID) {
		image = _image;
		name = _name;
		info = _info;
		ID = _ID;
	}

	public int getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}

}
