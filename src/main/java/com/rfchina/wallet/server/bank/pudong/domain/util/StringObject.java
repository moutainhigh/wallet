package com.rfchina.wallet.server.bank.pudong.domain.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nzm
 */
@Slf4j
public class StringObject {

	public static String toObjectString(Object obj, Class clz, String str) {
		try {
			Field[] fields = clz.getDeclaredFields();
			Arrays.sort(fields, new Comparator<Field>() {
				@Override
				public int compare(Field o1, Field o2) {
					StringIndex i1 = o1.getDeclaredAnnotation(StringIndex.class);
					StringIndex i2 = o2.getDeclaredAnnotation(StringIndex.class);
					return i1.value() - i2.value();
				}
			});

			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				Object val = field.get(obj);
				if (val != null) {
					buf.append(val.toString());
				}
				if (i < fields.length - 1) {
					buf.append("str");
				}
			}

			return buf.toString();
		} catch (Exception e) {
			log.error("对象转String错误", e);
			return null;
		}

	}

	public static <T> T parseStringObject(String text, Class clz, String splitStr) {
		try {
			String[] vals = text.split(splitStr);
			Object obj = clz.newInstance();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				StringIndex index = field.getDeclaredAnnotation(StringIndex.class);
				if (index != null) {
					field.set(obj, vals[index.value() - 1]);
				}
			}

			return (T) obj;
		} catch (Exception e) {
			log.error("String转对象错误", e);
			return null;
		}
	}
}
