/**
 * 
 */
package android.enums;

/**
 *****************************************************************************************************************************************************************************
 * 字体调置
 * @author :Atar
 * @createTime:2015-2-5上午10:45:00
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public enum TypefaceMode {
	/* 字体默认key-value的key */
	TYPE_FACE_MODE_KEY("Type_face_Mode_KEY"),
	/* 字体中 */
	MIDDLE("M"),
	/* 字体小 */
	SMALL_MODE("S"),
	/* 字体大 */
	LARGER_MODE("L"),
	/* 超大 */
	GREAT_MODE("G"),
	/* 巨大 */
	BIG_MODE("B");
	// /* 巨无霸大 */
	// LAGRE_BIG_MODE("LB");
	private String value;

	TypefaceMode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
