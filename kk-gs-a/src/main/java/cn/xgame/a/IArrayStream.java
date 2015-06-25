package cn.xgame.a;

public interface IArrayStream {
	
	/**
	 * 通过数据库 获取数据
	 * @param data
	 */
	void fromBytes( byte[] data );
	
	/**
	 * 打包成数据库可用数据
	 * @return
	 */
	byte[] toBytes();
	
}
