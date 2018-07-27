package cmn.deploy.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import cmn.deploy.exception.BizException;


public class StringUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

	public StringUtil() {
	}

	/**
	 * limit
	 *
	 * @param str
	 * @param limit
	 * @return 변환된 문자열
	 * @exception Exception
	 */
	public static String shortCutString(String str, int limit) throws Exception {

		try {

			if (str == null || limit < 4)
				return str;

			int len = str.length();
			int cnt = 0, index = 0;

			while (index < len && cnt < limit) {
				if (str.charAt(index++) < 256) {
					cnt++;
				} else {
					cnt += 2;
				}
			}

			if (index < len)
				str = str.substring(0, index) + "...";

		} catch (Exception e) {
			throw new Exception("[StringUtil][shortCutString]" + e.getMessage(), e);
		}

		return str;

	}

	/**
	 * @param strTarget
	 * @param strDelete
	 * @exception Exception
	 * @return 삭제 후 문자열
	 */
	public static String delete(String strTarget, String strDelete) throws Exception {
		return replace(strTarget, strDelete, "");
	}

	/**
	 * <PRE>
	 * 숫자를 천단위로 "," 구분자를 추가하여 변환
	 * </PRE>
	 * 
	 * @param amount
	 * @return 숫자를 천단위로 변환한 결과 ex)"1,234","200,324"
	 */
	public static String getAmount(String amount) {

		String convertedAmount = "";
		if (amount != null && amount.length() != 0) {

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < amount.length(); i++) {
				int j = (amount.length() - (i + 1)) % 3;

				if (i != (amount.length() - 1) && j == 0) {
					buffer.append(amount.charAt(i));
					buffer.append(",");
				} else {
					buffer.append(amount.charAt(i));
				}
			}
			convertedAmount = buffer.toString();
		}

		return convertedAmount;
	}

	/**
	 * <PRE>
	 * 입력 스트링을 검사하여 null 이면 "" 인 스트링을 반환
	 * </PRE>
	 * 
	 * @param test
	 *            String
	 * @return 입력 스트링이 null이면 공백문자열을 반환
	 */
	public static String null2EmptyString(String test) {

		return (test == null ? "" : test);
	}

	/**
	 * 전달받은 숫자를 지정된 형태로 출력한다. 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR>
	 * <BR>
	 *
	 * 사용예) getFormattedNumber(1, "00000")<BR>
	 * 결 과 ) "00001"<BR>
	 * <BR>
	 *
	 * @param pInstr
	 *            long
	 * @return String
	 */
	public static String getFormmatedNumber(long num, String format) {
		StringBuffer formattedNum = new StringBuffer();
		String strNum = "" + num;

		if (format == null) {
			return strNum;
		}

		try {
			for (int i = 0; i < format.length() - strNum.length(); i++) {
				formattedNum.append(format.charAt(i));
			}
			formattedNum.append(strNum);
		} catch (Exception e) {
		}
		;

		return formattedNum.toString();
	}

	/**
	 * 전달받은 숫자를 지정된 형태로 출력한다. 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR>
	 * <BR>
	 *
	 * 사용예) getFormattedNumber(1, "00000")<BR>
	 * 결 과 ) "00001"<BR>
	 * <BR>
	 *
	 * @param pInstr
	 *            long
	 * @return String
	 */
	public static String getFormatedNumber(int num, String format) {
		StringBuffer formattedNum = new StringBuffer();
		String strNum = "" + num;

		if (format == null) {
			return strNum;
		}

		try {
			for (int i = 0; i < format.length() - strNum.length(); i++) {
				formattedNum.append(format.charAt(i));
			}
			formattedNum.append(strNum);
		} catch (Exception e) {
		}
		;

		return formattedNum.toString();
	}

	public static boolean isDigit(String digitStr) {
		if (null == digitStr || digitStr.length() < 1) {
			return false;
		}

		return digitStr.replaceAll("\\D", "").length() == digitStr.length();
	}

	/**
	 * 문자열을 원하는 길이만큼 지정한 문자로 padding 처리한다.
	 *
	 * @param origin
	 *            padding 처리할 문자열
	 * @param limit
	 *            padding 처리할 범위
	 * @param pad
	 *            padding 될 문자
	 * @return padding 처리된 문자열
	 */
	public static String padding(String origin, int limit, String pad) {

		String originStr = "";
		if (origin != null) {
			originStr = origin;
		}

		String padStr = "";
		if (pad != null) {
			padStr = pad;
		}

		int size = origin.length();

		if (limit <= size) {
			return originStr;

		} else {
			StringBuffer sb = new StringBuffer(originStr);

			for (int inx = size; inx < limit; inx++) {
				sb.append(padStr);
			}

			return sb.toString();
		}
	}

	/**
	 * 문자열을 원하는 길이만큼 지정한 문자로 left padding 처리한다.
	 *
	 * @param origin
	 *            padding 처리할 문자열
	 * @param limit
	 *            padding 처리할 size
	 * @param pad
	 *            padding 될 문자
	 * @return padding 처리된 문자열
	 */
	public static String leftPadding(String origin, int limit, String pad) {
		String temp = pad;
		if (pad == null) {
			temp = "";
		}

		String originStr = "";
		if (origin != null) {
			originStr = origin;
		}

		int size = originStr.length();

		if (limit <= size) {
			return originStr;

		} else {
			StringBuffer sb = new StringBuffer(temp);

			for (int inx = size + 1; inx < limit; inx++) {
				sb.append(temp);
			}

			return sb.append(originStr).toString();
		}
	}

	/**
	 * 해당문자열 중 특정 문자열을 치환한다. <BR>
	 * <BR>
	 *
	 * 사용예) replaceSecOutput("&<>#\'" )<BR>
	 * 결 과 ) &amp;&lt;&gt;&#35;&quot;&#39;<BR>
	 * <BR>
	 *
	 * @param pInstr
	 *            String
	 * @return String 치환된 문자열
	 */
	public static String replaceSecOutput(String pInstr) {

		if (pInstr == null || pInstr.equals("")) {
			return "";
		}

		String result = pInstr;
		try {
			result = replace(result, "&", "&amp;");
			result = replace(result, "<", "&lt;");
			result = replace(result, ">", "&gt;");
			result = replace(result, "#", "&#35;");
			result = replace(result, "\"", "&quot;");
			result = replace(result, "'", "&#39;");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return pInstr;
		}

		return result;
	}

	/**
	 * 해당문자열 중 특정 문자열을 치환한다. <BR>
	 * <BR>
	 *
	 * 사용예) replaceSecOutput("&<>#\'" )<BR>
	 * 결 과 ) &amp;&lt;&gt;&#35;&quot;&#39;<BR>
	 * <BR>
	 *
	 * @param pInstr
	 *            String
	 * @return String 치환된 문자열
	 */
	public static String replaceSecXmlKeyOutput(String pInstr) {

		if (pInstr == null || pInstr.equals("")) {
			return "_";
		}

		String result = pInstr;
		if (!isValidXmlKey(pInstr)) {
			result = pInstr.trim();
			result = result.substring(0, 1).replaceFirst("[^a-zA-Z_]", "_")
					+ result.substring(1).replaceAll("[^a-zA-Z0-9_.-]", "_");
		}

		return result;
	}

	/**
	 * XML 엘리먼트 명으로 합당한지 여부 체크 1. 엘리먼트는 전체가 문자, 숫자, 특수기호(., _, -)로만 이루어져야 한다. 2.
	 * 첫 시작은 문자, _ 만이 가능하다. (공백 안 됨) 편의상 영문자로 제한을 한다.
	 *
	 * @param pInstr
	 * @return
	 */
	public static boolean isValidXmlKey(String pInstr) {
		if (pInstr == null || pInstr.trim().length() < 1) {
			return false;
		}

		return pInstr.matches("[a-zA-Z_][a-zA-Z0-9_.-]*");
	}

	/**
	 * 해당문자열 중 특정 문자열을 Javascript에서 사용할수 있도록 치환한다. <BR>
	 * <BR>
	 *
	 * 사용예) replaceJSEncodeOutput(""'" )<BR>
	 * 결 과 ) %22%27<BR>
	 * <BR>
	 *
	 * @param pInstr
	 *            String
	 * @return String 치환된 문자열
	 */
	public static String replaceJSEncodeOutput(String pInstr) {

		if (pInstr == null || pInstr.equals("")) {
			return "";
		}

		String result = pInstr;

		try {
			result = replace(result, "\"", "%22");
			result = replace(result, "'", "%27");
		} catch (Exception e) {
			return pInstr;
		}

		return result;
	}

	/**
	 * 영문을 한글로 Conversion해주는 Method. (8859_1 --> KSC5601)
	 * 
	 * @param english
	 *            한글로 바꾸어질 영문 String
	 * @return 한글로 바꾸어진 String
	 */
	public static synchronized String koreanForPortal(String english) {
		String korean = null;

		if (english == null) {
			return null;
		}

		try {
			korean = new String(english.getBytes("8859_1"), "euc-kr");
		} catch (UnsupportedEncodingException e) {
			korean = new String(english);
		}
		return korean;
	}

	/**
	 * 숫자 앞의 빈칸을 자릿수만큼 메꿔주는 Method. ex) blankToString( "1", 4, "0"); "1" ->
	 * "0001"
	 * 
	 * @param orig
	 *            빈칸 채우기 전의 본래 String
	 * @param length
	 *            문자의 자릿수 int
	 * @param add
	 *            빈칸을 채울 문자 String
	 * @return 빈칸이 채워진 String
	 */
	public static String blankToString(String orig, int length, String add) {
		if (orig == null) {
			orig = "";
		}
		int space = length - orig.length();

		int i = 0;

		String buf = "";

		for (i = 0; i < space; i++)

			buf += add;

		orig = buf + orig;

		return orig;

	}

	/**
	 * 숫자 앞의 빈칸을 자릿수만큼 0으로 메꿔주는 Method. ex) blankToZero( "1", 4 ); "1" -> "0001"
	 * 
	 * @param orig
	 *            빈칸 채우기 전의 본래 String
	 * @param length
	 *            문자의 자릿수 int
	 * @return 빈칸이 채워진 String
	 */
	public static String blankToZero(String orig, int length) {
		String num = "";
		num = blankToString(orig, length, "0");

		return num;

	}

	/**
	 * String 배열 객체를 toString 하는 메소드 ex) [AAA,BBB,CCC]
	 *
	 * @param inAraayStr
	 *            출력할 String 배열
	 * @return toString
	 */
	public static String toArrayString(String[] inAraayStr) {
		String result = null;
		if (inAraayStr != null) {
			StringBuffer sb = new StringBuffer();

			sb.append("[");
			for (int i = 0; i < inAraayStr.length; i++) {
				sb.append(inAraayStr[i]);
				if (i < inAraayStr.length - 1)
					sb.append(",");
			}
			sb.append("]");

			result = sb.toString();
		}

		return result;
	}

	/**
	 * int 배열 객체를 toString 하는 메소드 ex) [AAA,BBB,CCC]
	 *
	 * @param inAraayInt
	 *            출력할 I 배열
	 * @return toString
	 */
	public static String toArrayString(int[] inAraayInt) {
		String result = null;
		if (inAraayInt != null) {
			StringBuffer sb = new StringBuffer();

			sb.append("[");
			for (int i = 0; i < inAraayInt.length; i++) {
				sb.append(String.valueOf(inAraayInt[i]));
				if (i < inAraayInt.length - 1)
					sb.append(",");
			}
			sb.append("]");

			result = sb.toString();
		}

		return result;
	}

	public static String lpad(int iSrc, int iDigit) {
		return lpad("" + iSrc, "0", iDigit);
	}

	public static String lpad(int iSrc, String strPaddingChar, int iDigit) {
		return lpad("" + iSrc, strPaddingChar, iDigit);
	}

	public static String lpad(String strSrc, int iDigit) {
		return lpad(strSrc, "0", iDigit);
	}

	public static String lpad(String strSrc, String strPaddingChar, int iDigit) {
		String strTmp = "";
		for (int i = 0; i < iDigit; i++)
			strTmp += strPaddingChar;
		return (strTmp + strSrc).substring((strTmp + strSrc).length() - iDigit);
	}

	/**
	 * <PRE>
	 *
	 *  사용예)
	 *  Object[] objArray   =   extractBlock("1^2^3^",'^');
	 *  objArray[0]     =   "1";
	 *  objArray[1]     =   "2";
	 *  objArray[2]     =   "3";
	 *  objArray[3]     =   "";
	 *
	 * &#64;param block
	 * &#64;param delimiter
	 * &#64;return Object[] 결과
	 * </PRE>
	 */
	public static Object[] extractBlock(String block, char delimiter) {
		List<String> v = new ArrayList<String>();
		String s = "";
		for (int ii = 0; ii < block.length(); ii++) {
			char c = block.charAt(ii);

			if (c == delimiter) {
				v.add(s);
				s = "";
			} else
				s += c;
		}
		v.add(s);
		return v.toArray();
	}

	/**
	 * <PRE>
	 *  사용예)
	 *  Object[] objArray   =   extractBlock("1^2^3^",'^');
	 *  objArray[0]     =   "1";
	 *  objArray[1]     =   "2";
	 *  objArray[2]     =   "3";
	 *  objArray[3]     =   "";
	 *
	 * &#64;param block
	 * &#64;param delimiter
	 * &#64;param escape
	 * &#64;param escapeOnlyDeletmeter
	 * &#64;return Object[] 결과
	 * </PRE>
	 */
	public static Object[] extractBlock(String block, char delimiter, char escape, boolean escapeOnlyDeletmeter) {
		List<String> v = new ArrayList<String>();
		String s = "";
		char beforeChar = ' ';
		char c = ' ';
		for (int ii = 0; ii < block.length(); ii++) {
			c = block.charAt(ii);

			if (escapeOnlyDeletmeter && beforeChar == escape) {
				s += escape;
			}

			if (c == delimiter) {
				if (beforeChar == escape) {
					s += c;
				} else {
					v.add(s);
					s = "";
				}
			} else if (c == escape) {
				if (beforeChar == escape) {
					s += c;
					beforeChar = ' ';
					continue;
				}

			} else {
				s += c;
			}

			beforeChar = c;
		}
		v.add(s);
		return v.toArray();
	}

	/**
	 * Vector 를 받아서 String[] 로 리턴한다. DaoGenerator 로 생성된 dao 에서 사용됨
	 *
	 * @param v
	 * @return Vector에서 String[]로 변환된 결과
	 */
	public static <V> String[] toArray(Vector<V> v) {
		String[] sa = new String[v.size()];
		v.copyInto(sa);
		return sa;
	}

	/**
	 * Vector 를 받아서 String[] 로 리턴한다. DaoGenerator 로 생성된 dao 에서 사용됨
	 *
	 * @param al
	 * @return ArrayList에서 String[]로 변환된 결과
	 */
	public static <V> String[] toArray(ArrayList<V> al) {

		int size = al.size();
		String[] paramList = null;
		if (size > 0) {
			paramList = new String[size];
			for (int i = 0; i < size; i++) {
				paramList[i] = (String) al.get(i);
			}
		} else {
			return new String[0];
		}
		return paramList;
	}

	/**
	 *
	 * 사용예) addTellFormat( "02", "567", "1234" )<BR>
	 * 결 과 ) 02-567-1234<BR>
	 * <BR>
	 *
	 * trustForm에서 칸칸이 따로 입력받은 node의 값을 DB에 넣기전 전화번호 포맷을 갖춰서 넣어준다.
	 *
	 * @param oneTellNo
	 * @param twoTellNo
	 * @param thrTellNo
	 * @return String로 조합된 전화번호 결과
	 */
	public static String addTellFormat(String oneTellNo, String twoTellNo, String thrTellNo) {

		StringBuffer rStr = new StringBuffer();

		try {
			rStr.append(oneTellNo);
			rStr.append("-");
			rStr.append(twoTellNo);
			rStr.append("-");
			rStr.append(thrTellNo);
		} catch (Exception e) {
		}
		;

		return rStr.toString().trim();
	}

	/**
	 *
	 * 사용예) removeTellFormat( "02-567-1234" )<BR>
	 * 결 과 ) String[] removeTell = { "02", "567", "1234" }<BR>
	 * <BR>
	 *
	 * DB에 저장된 전화번호를 "-"를 떼고 String[]으로 각각 전화번호를 세등분하여 return한다.
	 * 
	 * @param tellNo
	 * @return String[]로 각각 분리된 전화번호 결과
	 */
	public static String[] removeTellFormat(String tellNo) {
		if (tellNo == null || tellNo.equals("")) {
			return null;
		}
		int first = tellNo.indexOf("-");
		String oneTellNo = tellNo.substring(0, first); // 첫번째 칸 전화번호 ( 02 )

		String twoThrTellNo = tellNo.substring(tellNo.indexOf("-") + 1); // 두번째부터
																			// 뒤까지
																			// 전화번호(567-1234)
		int two = twoThrTellNo.indexOf("-"); //

		String twoTellNo = twoThrTellNo.substring(0, two); // 두번째 칸 전화번호 (567)
		String threeTellNo = twoThrTellNo.substring(two + 1); // 세번째 칸 전화번호
																// (1234)

		String[] removeTell = { oneTellNo, twoTellNo, threeTellNo };

		return removeTell;
	}

	/**
	 * 특정 String 내의 일정한 pattern subString을 replace 문자열로 대치한다.
	 *
	 * 사용예) replace("2002-02-10", "-", "/")<BR>
	 * 결 과 ) "2002/02/10"<BR>
	 * <BR>
	 * 
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return 패턴이 변형된 String 결과
	 */
	public static String replace(String str, String pattern, String replace) {
		int s = 0, e = 0;

		if (str == null || str.equals(""))
			return "";

		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}

		result.append(str.substring(s));
		return result.toString();
	}

	/**
	 * xecure web 관련 암호화 데이터의 앞뒤에 특수문자 세팅
	 * 
	 * @param cipher
	 *            키 값
	 * @return String
	 */
	public static String makeCipherFlag(String cipher) {
		return "◐" + cipher + "◑";
	}

	/**
	 * vector에 다른 vector의 내용을 더해서 반환함.
	 * 
	 * @param v1
	 *            원본 vector.
	 * @param v2
	 *            <code>v1</code>에 아이템을 더하고자 하는 벡터.
	 * @return <code>v1</code>
	 */
	public static <V> Vector<V> appendVector(Vector<V> v1, Vector<V> v2) {
		if ((v2 == null) || (v2.size() == 0))
			return (v1);
		Enumeration<V> e = v2.elements();
		while (e.hasMoreElements())
			v1.addElement(e.nextElement());
		return (v1);
	}

	/**
	 * Float를 String으로 변환.
	 * 
	 * @param Float에서
	 *            String으로 변환된 문자열
	 * @return String
	 */
	public static String toString(Float f) {
		DecimalFormat df = new DecimalFormat("#0.0#################");
		return df.format(f.floatValue());
	}

	/**
	 * Double를 String으로 변환.
	 * 
	 * @param Double에서
	 *            String으로 변환된 문자열
	 * @return String
	 */
	public static String toString(Double d) {
		DecimalFormat df = new DecimalFormat("#0.0#################");
		return df.format(d.doubleValue());
	}

	/**
	 * Object를 String으로 변환.
	 * 
	 * @param Object에서
	 *            String으로 변환된 문자열
	 * @return String
	 */
	public static String toString(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * 문자열이 지정한 길이를 초과했을때 지정한길이에다가 해당 문자열을 붙여주는 메서드.
	 * 
	 * @param source
	 *            원본 문자열 배열
	 * @param output
	 *            더할문자열
	 * @param slength
	 *            지정길이
	 * @return 지정길이로 잘라서 더할분자열 합친 문자열
	 */
	public static String cutString(String source, String output, int slength) {
		String returnVal = null;
		if (source != null) {
			if (source.length() > slength) {
				returnVal = source.substring(0, slength) + output;
			} else {
				returnVal = source;
			}
		}
		return returnVal;
	}

	/**
	 * 문자열이 지정한 길이를 초과했을때 해당 문자열을 삭제하는 메서드
	 * 
	 * @param source
	 *            원본 문자열 배열
	 * @param slength
	 *            지정길이
	 * @return 지정길이로 잘라서 더할분자열 합친 문자열
	 */
	public static String cutString(String source, int slength) {
		String result = null;
		if (source != null) {
			if (source.length() > slength) {
				result = source.substring(0, slength);
			} else {
				result = source;
			}
		}
		return result;
	}

	/**
	 * <p>
	 * String이 비었거나("") 혹은 null 인지 검증한다.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtil.isEmpty(null)      = true
	 *  StringUtil.isEmpty("")        = true
	 *  StringUtil.isEmpty(" ")       = false
	 *  StringUtil.isEmpty("bob")     = false
	 *  StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            - 체크 대상 스트링오브젝트이며 null을 허용함
	 * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
	 */
	public static boolean isEmpty(String str) {
		if (str != null) {
			str = str.trim();
		}
		return str == null || str.length() == 0;
	}

	/**
	 * <p>
	 * 기준 문자열에 포함된 모든 대상 문자(char)를 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.remove(null, *)       = null
	 * StringUtil.remove("", *)         = ""
	 * StringUtil.remove("queued", 'u') = "qeed"
	 * StringUtil.remove("queued", 'z') = "queued"
	 * </pre>
	 *
	 * @param str
	 *            입력받는 기준 문자열
	 * @param remove
	 *            입력받는 문자열에서 제거할 대상 문자열
	 * @return 제거대상 문자열이 제거된 입력문자열. 입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String remove(String str, char remove) {
		if (str == null || (str != null && str.equals(""))) {
			return "";
		}

		if (isEmpty(str) || str.indexOf(remove) == -1) {
			return str;
		}
		char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	/**
	 * <p>
	 * 문자열 내부의 콤마 character(,)를 모두 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.removeCommaChar(null)       = null
	 * StringUtil.removeCommaChar("")         = ""
	 * StringUtil.removeCommaChar("asdfg,qweqe") = "asdfgqweqe"
	 * </pre>
	 *
	 * @param str
	 *            입력받는 기준 문자열
	 * @return " , "가 제거된 입력문자열 입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String removeCommaChar(String str) {
		return remove(str, ',');
	}

	/**
	 * <p>
	 * 문자열 내부의 마이너스 character(-)를 모두 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.removeMinusChar(null)       = null
	 * StringUtil.removeMinusChar("")         = ""
	 * StringUtil.removeMinusChar("a-sdfg-qweqe") = "asdfgqweqe"
	 * </pre>
	 *
	 * @param str
	 *            입력받는 기준 문자열
	 * @return " - "가 제거된 입력문자열 입력문자열이 null인 경우 출력문자열은 null
	 */
	public static String removeMinusChar(String str) {
		return remove(str, '-');
	}

	/**
	 * 원본 문자열의 포함된 특정 문자열 첫번째 한개만 새로운 문자열로 변환하는 메서드
	 * 
	 * @param source
	 *            원본 문자열
	 * @param subject
	 *            원본 문자열에 포함된 특정 문자열
	 * @param object
	 *            변환할 문자열
	 * @return sb.toString() 새로운 문자열로 변환된 문자열 / source 특정문자열이 없는 경우 원본 문자열
	 */
	public static String replaceOnce(String source, String subject, String object) {
		if (source == null || (source != null && source.equals(""))) {
			return "";
		}
		if (subject == null || (subject != null && subject.equals(""))) {
			return "";
		}
		if (object == null) {
			return "";
		}

		StringBuffer rtnStr = new StringBuffer();
		String preStr = "";
		String nextStr = source;
		if (source.indexOf(subject) >= 0) {
			preStr = source.substring(0, source.indexOf(subject));
			nextStr = source.substring(source.indexOf(subject) + subject.length(), source.length());
			rtnStr.append(preStr).append(object).append(nextStr);
			return rtnStr.toString();
		} else {
			return source;
		}
	}

	/**
	 * <code>subject</code>에 포함된 각각의 문자를 object로 변환한다.
	 * 
	 * @param source
	 *            원본 문자열
	 * @param subject
	 *            원본 문자열에 포함된 특정 문자열
	 * @param object
	 *            변환할 문자열
	 * @return sb.toString() 새로운 문자열로 변환된 문자열
	 */
	public static String replaceChar(String source, String subject, String object) {
		if (source == null || (source != null && source.equals(""))) {
			return "";
		}
		if (subject == null || (subject != null && subject.equals(""))) {
			return "";
		}
		if (object == null) {
			return "";
		}

		StringBuffer rtnStr = new StringBuffer();
		String preStr = "";
		String nextStr = source;
		String srcStr = source;

		char chA;

		int subjectCnt = subject.length();

		for (int i = 0; i < subjectCnt; i++) {
			chA = subject.charAt(i);

			if (srcStr.indexOf(chA) >= 0) {
				preStr = srcStr.substring(0, srcStr.indexOf(chA));
				nextStr = srcStr.substring(srcStr.indexOf(chA) + 1, srcStr.length());
				srcStr = rtnStr.append(preStr).append(object).append(nextStr).toString();
			}
		}

		return srcStr;
	}

	/**
	 * <p>
	 * <code>str</code> 중 <code>searchStr</code>의 시작(index) 위치를 반환.
	 * </p>
	 *
	 * <p>
	 * 입력값 중 <code>null</code>이 있을 경우 <code>-1</code>을 반환.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.indexOf(null, *)          = -1
	 * StringUtil.indexOf(*, null)          = -1
	 * StringUtil.indexOf("", "")           = 0
	 * StringUtil.indexOf("aabaabaa", "a")  = 0
	 * StringUtil.indexOf("aabaabaa", "b")  = 2
	 * StringUtil.indexOf("aabaabaa", "ab") = 1
	 * StringUtil.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 *
	 * @param str
	 *            검색 문자열
	 * @param searchStr
	 *            검색 대상문자열
	 * @return 검색 문자열 중 검색 대상문자열이 있는 시작 위치 검색대상 문자열이 없거나 null인 경우 -1
	 */
	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	/**
	 * <p>
	 * 오라클의 decode 함수와 동일한 기능을 가진 메서드이다. <code>sourStr</code>과
	 * <code>compareStr</code>의 값이 같으면 <code>returStr</code>을 반환하며, 다르면
	 * <code>defaultStr</code>을 반환한다.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.decode(null, null, "foo", "bar")= "foo"
	 * StringUtil.decode("", null, "foo", "bar") = "bar"
	 * StringUtil.decode(null, "", "foo", "bar") = "bar"
	 * StringUtil.decode("하이", "하이", null, "bar") = null
	 * StringUtil.decode("하이", "하이  ", "foo", null) = null
	 * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
	 * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
	 * </pre>
	 * 
	 * @param sourceStr
	 *            비교할 문자열
	 * @param compareStr
	 *            비교 대상 문자열
	 * @param returnStr
	 *            sourceStr와 compareStr의 값이 같을 때 반환할 문자열
	 * @param defaultStr
	 *            sourceStr와 compareStr의 값이 다를 때 반환할 문자열
	 * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며, <br/>
	 *         다르면 defaultStr을 반환한다.
	 */
	public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
		if (sourceStr == null && compareStr == null) {
			return returnStr;
		}

		if (sourceStr == null && compareStr != null) {
			return defaultStr;
		}

		if (sourceStr.trim().equals(compareStr)) {
			return returnStr;
		}

		return defaultStr;
	}

	/**
	 * <p>
	 * 오라클의 decode 함수와 동일한 기능을 가진 메서드이다. <code>sourStr</code>과
	 * <code>compareStr</code>의 값이 같으면 <code>returStr</code>을 반환하며, 다르면
	 * <code>sourceStr</code>을 반환한다.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.decode(null, null, "foo") = "foo"
	 * StringUtil.decode("", null, "foo") = ""
	 * StringUtil.decode(null, "", "foo") = null
	 * StringUtil.decode("하이", "하이", "foo") = "foo"
	 * StringUtil.decode("하이", "하이 ", "foo") = "하이"
	 * StringUtil.decode("하이", "바이", "foo") = "하이"
	 * </pre>
	 * 
	 * @param sourceStr
	 *            비교할 문자열
	 * @param compareStr
	 *            비교 대상 문자열
	 * @param returnStr
	 *            sourceStr와 compareStr의 값이 같을 때 반환할 문자열
	 * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며, <br/>
	 *         다르면 sourceStr을 반환한다.
	 */
	public static String decode(String sourceStr, String compareStr, String returnStr) {
		return decode(sourceStr, compareStr, returnStr, sourceStr);
	}

	/**
	 * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
	 * 
	 * @param object
	 *            원본 객체
	 * @return resultVal 문자열
	 */
	public static String isNullToString(Object object) {
		String string = "";

		if (object != null) {
			string = object.toString().trim();
		}

		return string;
	}

	/**
	 * <pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 * </pre>
	 */
	public static String nullConvert(Object src) {
		// if (src != null &&
		// src.getClass().getName().equals("java.math.BigDecimal")) {
		if (src != null && src instanceof java.math.BigDecimal) {
			return ((BigDecimal) src).toString();
		}

		if (src == null || src.equals("null")) {
			return "";
		} else {
			return ((String) src).trim();
		}
	}

	/**
	 * <pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 * </pre>
	 */
	public static String nullConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
			return "";
		} else {
			return src.trim();
		}
	}

	/**
	 * <pre>
	 * 인자로 받은 String이 null일 경우 &quot;0&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;0&quot;로 바꾼 String 값.
	 * </pre>
	 */
	public static int zeroConvert(Object src) {

		if (src == null || src.equals("null")) {
			return 0;
		} else {
			return Integer.parseInt(((String) src).trim());
		}
	}

	/**
	 * <pre>
	 * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
	 * &#064;param src null값일 가능성이 있는 String 값.
	 * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
	 * </pre>
	 */
	public static int zeroConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
			return 0;
		} else {
			return Integer.parseInt(src.trim());
		}
	}

	/**
	 * <p>
	 * 문자열에서 {@link Character#isWhitespace(char)}에 정의된 모든 공백문자를 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.removeWhitespace(null)         = null
	 * StringUtil.removeWhitespace("")           = ""
	 * StringUtil.removeWhitespace("abc")        = "abc"
	 * StringUtil.removeWhitespace("   ab  c  ") = "abc"
	 * </pre>
	 *
	 * @param str
	 *            공백문자가 제거도어야 할 문자열
	 * @return the 공백문자가 제거된 문자열, null이 입력되면 <code>null</code>이 리턴
	 */
	public static String removeWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}

		return new String(chs, 0, count);
	}

	/**
	 * Html 코드가 들어간 문서를 표시할때 태그에 손상없이 보이기 위한 메서드
	 * 
	 * @param strString
	 * @return HTML 태그를 치환한 문자열
	 */
	public static String checkHtmlView(String strString) {
		if (strString == null || (strString != null && strString.equals(""))) {
			return "";
		}

		String strNew = "";

		try {
			StringBuffer strTxt = new StringBuffer("");

			char chrBuff;
			int len = strString.length();

			for (int i = 0; i < len; i++) {
				chrBuff = (char) strString.charAt(i);

				switch (chrBuff) {
				case '<':
					strTxt.append("&lt;");
					break;
				case '>':
					strTxt.append("&gt;");
					break;
				case '"':
					strTxt.append("&quot;");
					break;
				case 10:
					strTxt.append("<br>");
					break;
				case ' ':
					strTxt.append("&nbsp;");
					break;
				// case '&' :
				// strTxt.append("&amp;");
				// break;
				default:
					strTxt.append(chrBuff);
				}
			}

			strNew = strTxt.toString();

		} catch (NullPointerException ex) {
			return null;
		} catch (Exception ex) {
			return null;
		}

		return strNew;
	}

	/**
	 * 문자열을 지정한 분리자에 의해 배열로 리턴하는 메서드.
	 * 
	 * @param source
	 *            원본 문자열
	 * @param separator
	 *            분리자
	 * @return result 분리자로 나뉘어진 문자열 배열
	 */
	public static String[] split(String source, String separator) throws NullPointerException {
		if (source == null || (source != null && source.equals(""))) {
			return null;
		}
		if (separator == null) {
			return null;
		}

		String[] returnVal = null;
		int cnt = 1;

		int index = source.indexOf(separator);
		int index0 = 0;

		while (index >= 0) {
			cnt++;
			index = source.indexOf(separator, index + 1);

			if (index < 0) {
				break;
			}
		}

		returnVal = new String[cnt];
		cnt = 0;
		index = source.indexOf(separator);

		while (index >= 0) {
			returnVal[cnt] = source.substring(index0, index);
			index0 = index + 1;
			index = source.indexOf(separator, index + 1);
			cnt++;

			if (index < 0) {
				break;
			}
		}
		returnVal[cnt] = source.substring(index0);

		return returnVal;
	}

	/**
	 * <p>
	 * {@link String#toLowerCase()}를 이용하여 소문자로 변환한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.lowerCase(null)  = null
	 * StringUtil.lowerCase("")    = ""
	 * StringUtil.lowerCase("aBc") = "abc"
	 * </pre>
	 *
	 * @param str
	 *            소문자로 변환되어야 할 문자열
	 * @return 소문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toLowerCase();
	}

	/**
	 * <p>
	 * {@link String#toUpperCase()}를 이용하여 대문자로 변환한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.upperCase(null)  = null
	 * StringUtil.upperCase("")    = ""
	 * StringUtil.upperCase("aBc") = "ABC"
	 * </pre>
	 *
	 * @param str
	 *            대문자로 변환되어야 할 문자열
	 * @return 대문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toUpperCase();
	}

	/**
	 * <p>
	 * 입력된 String의 앞쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.stripStart(null, *)          = null
	 * StringUtil.stripStart("", *)            = ""
	 * StringUtil.stripStart("abc", "")        = "abc"
	 * StringUtil.stripStart("abc", null)      = "abc"
	 * StringUtil.stripStart("  abc", null)    = "abc"
	 * StringUtil.stripStart("abc  ", null)    = "abc  "
	 * StringUtil.stripStart(" abc ", null)    = "abc "
	 * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
	 * </pre>
	 *
	 * @param str
	 *            지정된 문자가 제거되어야 할 문자열
	 * @param stripChars
	 *            제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}

		return str.substring(start);
	}

	/**
	 * <p>
	 * 입력된 String의 뒤쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.stripEnd(null, *)          = null
	 * StringUtil.stripEnd("", *)            = ""
	 * StringUtil.stripEnd("abc", "")        = "abc"
	 * StringUtil.stripEnd("abc", null)      = "abc"
	 * StringUtil.stripEnd("  abc", null)    = "  abc"
	 * StringUtil.stripEnd("abc  ", null)    = "abc"
	 * StringUtil.stripEnd(" abc ", null)    = " abc"
	 * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
	 * </pre>
	 *
	 * @param str
	 *            지정된 문자가 제거되어야 할 문자열
	 * @param stripChars
	 *            제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}

		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}

		return str.substring(0, end);
	}

	/**
	 * <p>
	 * 입력된 String의 앞, 뒤에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.strip(null, *)          = null
	 * StringUtil.strip("", *)            = ""
	 * StringUtil.strip("abc", null)      = "abc"
	 * StringUtil.strip("  abc", null)    = "abc"
	 * StringUtil.strip("abc  ", null)    = "abc"
	 * StringUtil.strip(" abc ", null)    = "abc"
	 * StringUtil.strip("  abcyx", "xyz") = "  abc"
	 * </pre>
	 *
	 * @param str
	 *            지정된 문자가 제거되어야 할 문자열
	 * @param stripChars
	 *            제거대상 문자열
	 * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
	 */
	public static String strip(String str, String stripChars) {
		if (isEmpty(str)) {
			return str;
		}

		String srcStr = str;
		srcStr = stripStart(srcStr, stripChars);

		return stripEnd(srcStr, stripChars);
	}

	/**
	 * 문자열을 지정한 분리자에 의해 지정된 길이의 배열로 리턴하는 메서드.
	 * 
	 * @param source
	 *            원본 문자열
	 * @param separator
	 *            분리자
	 * @param arraylength
	 *            배열 길이
	 * @return 분리자로 나뉘어진 문자열 배열
	 */
	public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
		if (source == null || (source != null && source.equals(""))) {
			return null;
		}
		if (separator == null) {
			return null;
		}

		String[] returnVal = new String[arraylength];
		int cnt = 0;
		int index0 = 0;
		int index = source.indexOf(separator);
		while (index >= 0 && cnt < (arraylength - 1)) {
			returnVal[cnt] = source.substring(index0, index);
			index0 = index + 1;
			index = source.indexOf(separator, index + 1);
			cnt++;
		}
		returnVal[cnt] = source.substring(index0);
		if (cnt < (arraylength - 1)) {
			for (int i = cnt + 1; i < arraylength; i++) {
				returnVal[i] = "";
			}
		}

		return returnVal;
	}

	/**
	 * 문자열을 다양한 문자셋(EUC-KR[KSC5601],UTF-8..)을 사용하여 인코딩하는 기능 역으로 디코딩하여 원래의 문자열을
	 * 복원하는 기능을 제공함 String temp = new String(문자열.getBytes("바꾸기전 인코딩"),"바꿀 인코딩");
	 * String temp = new String(문자열.getBytes("8859_1"),"KSC5601"); => UTF-8 에서
	 * EUC-KR
	 * 
	 * @param srcString
	 *            - 문자열
	 * @param srcCharsetNm
	 *            - 원래 CharsetNm
	 * @param charsetNm
	 *            - CharsetNm
	 * @return 인(디)코딩 문자열
	 * @exception MyException
	 * @see
	 */
	public static String getEncdDcd(String srcString, String srcCharsetNm, String cnvrCharsetNm) {

		String rtnStr = null;

		if (srcString == null) {
			return null;
		}

		try {
			rtnStr = new String(srcString.getBytes(srcCharsetNm), cnvrCharsetNm);
		} catch (UnsupportedEncodingException e) {
			rtnStr = null;
		}

		return rtnStr;
	}

	/**
	 * 특수문자를 웹 브라우저에서 정상적으로 보이기 위해 특수문자를 처리('<' -> & lT)하는 기능이다
	 * 
	 * @param srcString
	 *            - '<'
	 * @return 변환문자열('<' -> "&lt"
	 * @exception MyException
	 * @see
	 */
	public static String getSpclStrCnvr(String srcString) {
		if (srcString == null || (srcString != null && srcString.equals(""))) {
			return null;
		}

		String rtnStr = null;

		try {
			StringBuffer strTxt = new StringBuffer("");

			char chrBuff;
			int len = srcString.length();

			for (int i = 0; i < len; i++) {
				chrBuff = (char) srcString.charAt(i);

				switch (chrBuff) {
				case '<':
					strTxt.append("&lt;");
					break;
				case '>':
					strTxt.append("&gt;");
					break;
				case '&':
					strTxt.append("&amp;");
					break;
				default:
					strTxt.append(chrBuff);
				}
			}

			rtnStr = strTxt.toString();
		} catch (NullPointerException ex) {
			rtnStr = null;
		} catch (Exception e) {
			rtnStr = null;
		}

		return rtnStr;
	}

	/**
	 * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
	 * 
	 * @param
	 * @return Timestamp 값
	 * @exception MyException
	 * @see
	 */
	public static String getTimeStamp() {
		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		try {
			SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.getDefault());
			Timestamp ts = new Timestamp(System.currentTimeMillis());

			rtnStr = sdfCurrent.format(ts.getTime());
		} catch (IllegalArgumentException ex) {
			rtnStr = null;
		} catch (NullPointerException ex) {
			rtnStr = null;
		} catch (Exception e) {
			rtnStr = null;
		}

		return rtnStr;
	}

	/**
	 * html의 특수문자를 표현하기 위해
	 * 
	 * @param srcString
	 * @return String
	 * @exception Exception
	 * @see
	 */
	public static String getHtmlStrCnvr(String srcString) {
		String tmpString = srcString;

		try {
			tmpString = tmpString.replaceAll("&lt;", "<");
			tmpString = tmpString.replaceAll("&gt;", ">");
			tmpString = tmpString.replaceAll("&amp;", "&");
			tmpString = tmpString.replaceAll("&nbsp;", " ");
			tmpString = tmpString.replaceAll("&apos;", "\'");
			tmpString = tmpString.replaceAll("&quot;", "\"");
		} catch (NullPointerException ex) {
			tmpString = "";
		} catch (Exception ex) {
			tmpString = "";
		}

		return tmpString;

	}

	/**
	 * <p>
	 * 날짜 형식의 문자열 내부에 마이너스 character(-)를 추가한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtil.addMinusChar("20100901") = "2010-09-01"
	 * </pre>
	 *
	 * @param date
	 *            입력받는 문자열
	 * @return " - "가 추가된 입력문자열
	 */
	public static String addMinusChar(String date) {
		if (date != null && date.length() == 8) {
			return date.substring(0, 4).concat("-").concat(date.substring(4, 6)).concat("-")
					.concat(date.substring(6, 8));
		} else {
			return "";
		}
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param date
	 *            Date String
	 * @return
	 */
	public static String delDelimeter(String value) {
		String[] delimiters = { "-", "/", "." };
		String result = value;
		if (value == null) {
			return "";
		}

		for (int i = 0; i < delimiters.length; i++) {
			result = replaceChars(result, delimiters[i], "");
		}
		return result;
	}

	/**
	 * <p>
	 * Replaces multiple characters in a String in one go. This method can also
	 * be used to delete characters.
	 * </p>
	 *
	 * <p>
	 * For example:<br />
	 * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>
	 * .
	 * </p>
	 *
	 * <p>
	 * A <code>null</code> string input returns <code>null</code>. An empty ("")
	 * string input returns an empty string. A null or empty set of search
	 * characters returns the input string.
	 * </p>
	 *
	 * <p>
	 * The length of the search characters should normally equal the length of
	 * the replace characters. If the search characters is longer, then the
	 * extra search characters are deleted. If the search characters is shorter,
	 * then the extra replace characters are ignored.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.replaceChars(null, *, *)           = null
	 * StringUtils.replaceChars("", *, *)             = ""
	 * StringUtils.replaceChars("abc", null, *)       = "abc"
	 * StringUtils.replaceChars("abc", "", *)         = "abc"
	 * StringUtils.replaceChars("abc", "b", null)     = "ac"
	 * StringUtils.replaceChars("abc", "b", "")       = "ac"
	 * StringUtils.replaceChars("abcba", "bc", "yz")  = "ayzya"
	 * StringUtils.replaceChars("abcba", "bc", "y")   = "ayya"
	 * StringUtils.replaceChars("abcba", "bc", "yzx") = "ayzya"
	 * </pre>
	 *
	 * @param str
	 *            String to replace characters in, may be null
	 * @param searchChars
	 *            a set of characters to search for, may be null
	 * @param replaceChars
	 *            a set of characters to replace, may be null
	 * @return modified String, <code>null</code> if null string input
	 * @since 2.0
	 */
	public static String replaceChars(String str, String searchChars, String replaceChars) {
		if (str == null || str.length() == 0 || searchChars == null || searchChars.length() == 0) {
			return str;
		}
		char[] chars = str.toCharArray();
		int len = chars.length;
		boolean modified = false;

		int isize = searchChars.length();

		for (int i = 0; i < isize; i++) {
			char searchChar = searchChars.charAt(i);
			if (replaceChars == null || i >= replaceChars.length()) {
				// delete
				int pos = 0;
				for (int j = 0; j < len; j++) {
					if (chars[j] != searchChar) {
						chars[pos++] = chars[j];
					} else {
						modified = true;
					}
				}
				len = pos;
			} else {
				// replace
				for (int j = 0; j < len; j++) {
					if (chars[j] == searchChar) {
						chars[j] = replaceChars.charAt(i);
						modified = true;
					}
				}
			}
		}
		if (modified == false) {
			return str;
		}
		return new String(chars, 0, len);
	}

	/**
	 * 
	 *<pre>
	 * Check pattern is matched
	 *</pre>
	 * @param patterns String[] patterns
	 * @param path String path
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isMatchPattern(String[] patterns, String path) throws Exception {
		if (NullUtil.isNull(patterns) || NullUtil.isNull(path)) {
			throw new BizException("Pattern and path is not null");
		}
		boolean isMatched = false;
		AntPathMatcher antPathMatcher = BeanUtil.getBean("antPathMatcher");
		
		for (String pattern : patterns) {
			if (antPathMatcher.match(pattern, path)) {
				isMatched = true;
				break;
			}
		}
		
		return isMatched;
	}

	/**
	 * 
	 *<pre>
	 * Check pattern is matched
	 *</pre>
	 * @param patterns String[] patterns
	 * @param path String path
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isMatchPattern(List<String> patterns, String path) throws Exception {
		if (NullUtil.isNull(patterns) || NullUtil.isNull(path)) {
			throw new BizException("Pattern and path is not null");
		}
		boolean isMatched = false;
		AntPathMatcher antPathMatcher = BeanUtil.getBean("antPathMatcher");
		
		for (String pattern : patterns) {
			if (antPathMatcher.match(pattern, path)) {
				isMatched = true;
				break;
			}
		}
		
		return isMatched;
	}
	
	
	/**
	 * 
	 *<pre>
	 * 1.Description: 마지막 문자 제거
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param value String 
	 * @param lastChar char Delete Last Char
	 * @return String
	 * @throws Exception
	 */
	public static String deleteLastChar(String value, char lastChar) throws Exception {
		String retValue = value;
				
		int size = value.length();
		char[] arrChar = value.toCharArray();
		
		if (arrChar[size - 1] == lastChar) {
			retValue = value.substring(0, size - 1);
		}
		return retValue;
	}
	

}