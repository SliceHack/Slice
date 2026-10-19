package ru.vidtu.iasfork;

public class Expression {
	public String expr;
	public int i;

	public Expression(String s) {
		this.expr = s.replaceAll("\\s+", "");
	}

	public char current() {
		return this.i >= this.expr.length() ? '?' : this.expr.charAt(this.i);
	}

	public double parse(int returnFlag) {
		try {
			if (returnFlag == 2) {
				if (this.current() == '-') {
					++this.i;
					return -this.parse(2);
				} else if (this.current() == '(') {
					++this.i;
					double x = this.parse(0);
					if (this.current() == ')') {
						++this.i;
					}

					if (this.current() == '^') {
						++this.i;
						x = Math.pow(x, this.parse(2));
					}

					return x;
				} else {
					int begin;
					for(begin = this.i; this.current() == '.' || Character.isDigit(this.current()); ++this.i) {
					}

					double x = Double.parseDouble(this.expr.substring(begin, this.i));
					if (this.current() == '^') {
						++this.i;
						x = Math.pow(x, this.parse(2));
					}

					return x;
				}
			} else {
				double x = this.parse(2);

				while(true) {
					while(this.current() != '*') {
						if (this.current() != '/') {
							if (returnFlag == 1) {
								return x;
							}

							while(true) {
								while(this.current() != '+') {
									if (this.current() != '-') {
										return x;
									}

									++this.i;
									x -= this.parse(1);
								}

								++this.i;
								x += this.parse(1);
							}
						}

						++this.i;
						x /= this.parse(2);
					}

					++this.i;
					x *= this.parse(2);
				}
			}
		} catch (Throwable t) {
			throw new IllegalArgumentException("Sowwy, but we're unable to calculate something: '" + this.expr + "' at pos " + this.i, t);
		}
	}
}
