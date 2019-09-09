package tblcheck.helper;

import java.util.Optional;

public class DataHelper<T> {

	public T getValueOrDefault(T val, T defaultVal) {
		if (Optional.ofNullable(val).isPresent()) {
			return val;
		} else {
			return defaultVal;
		}
	}
}
