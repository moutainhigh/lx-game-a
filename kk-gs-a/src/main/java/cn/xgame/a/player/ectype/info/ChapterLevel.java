package cn.xgame.a.player.ectype.info;

public class ChapterLevel {
	public int id;
	public byte size;
	public byte curIndex;
	
	public void next() {
		if( curIndex+1 <= size ){
			curIndex += 1;
		}
	}
}
