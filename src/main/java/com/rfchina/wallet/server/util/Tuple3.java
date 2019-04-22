package com.rfchina.wallet.server.util;

import java.util.Objects;

public class Tuple3<L, M, R> {

	public L left;
	public M mid;
	public R right;

	public Tuple3() {
	}

	public Tuple3(L left, M mid, R right) {
		super();
		this.left = left;
		this.mid = mid;
		this.right = right;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Tuple3<?, ?, ?> tuple = (Tuple3<?, ?, ?>) o;
		return Objects.equals(left, tuple.left) && Objects.equals(mid, tuple.mid)
			&& Objects.equals(right, tuple.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public String toString() {
		return "Tuple{" + "left=" + left + ", mid=" + mid + ", right=" + right + '}';
	}
}
