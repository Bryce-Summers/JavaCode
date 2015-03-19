package BryceMath;

import static BryceMath.Factories.NumberFactory.C;
import static BryceMath.Factories.NumberFactory.E;
import static BryceMath.Factories.NumberFactory.I;
import static BryceMath.Factories.NumberFactory.M;
import static BryceMath.Factories.NumberFactory.R;
import static util.Print.print;
import static util.testing.ASSERT;
import BryceMath.Factories.ExpressionFactory;
import BryceMath.Numbers.BaseNumber;
import BryceMath.Numbers.Complex;
import BryceMath.Numbers.Equation;
import BryceMath.Numbers.Expression;
import BryceMath.Numbers.IntB;
import BryceMath.Numbers.Multinomial;
import BryceMath.Numbers.Rational;
//- Useful testing functions.
// -- Useful Number construction functions.

/* BryceMath testing Class.
 * 
 * Rewritten by Bryce Summers 5 - 30 - 2013
 * 
 * Purpose : These unit tests are used to imply the correctness
 * 			 of my Exact math and other mathematical structures.
 * 
 * Tests :	1. Numbers
 */

// FIXME : Test the toInt(), isInt(), and toIntB() functions.

public class test_numbers
{
	
	static final int TEST_SIZE = 10000;
	
	public test_numbers()
	{
		// Utility class.
		test_BaseNumber();
		
		// Number classes.
		test_BigIntegers();
		test_Rationals();
		test_Complex();
		test_Multinomials();
		
		//test_RationalMultinomials();
		
		test_Expressions();
		
		test_Equations();
		
		test_conj();
		
		print("All numeric tests passed!");
	}

	// - Internal testing functions.

	private void test_BaseNumber()
	{
		BaseNumber result = new BaseNumber(10);
		
		ASSERT(result.toString().equals("0"));
		result.add(5);
		ASSERT(result.toString().equals("5"));
		
		result.mult(4);
		ASSERT(result.toString().equals("20"));
		
		result.mult(20);
		ASSERT(result.toString().equals("400"));
		
		result.mult(347);
		ASSERT(result.toString().equals("138800"));
		
		result = new BaseNumber(16);
		result.add(87);
		ASSERT(result.toString().equals("57"));
		
		// 6152.
		result = new BaseNumber(2);
		result.add(6);
		
		// 6.
		ASSERT(result.toString().equals("110"));
		
		result.mult(10);
		result.add(1);
		// 61.
		ASSERT(result.toString().equals("111101"));
		
		result.mult(10);
		result.add(5);
		
		// 615
		ASSERT(result.toString().equals("1001100111"));
		
		result.mult(10);
		
		// 6150
		ASSERT(result.toString().equals("1100000000110"));
		
		result.add(2);
		
		// 6152
		ASSERT(result.toString().equals("1100000001000"));

	}
	
	private void test_BigIntegers()
	{
		// -- First test small numbers, the toString(),
		//    equals(), eq() functions and the identities.
		
		// Test -2.
		final IntB in2 = I(-2);
		ASSERT(in2.toString().equals("-2"));
		
		// Test -1.
		final IntB in1 = I(-1);
		
		ASSERT(in1.eq(IntB.ONE.neg()));
		ASSERT(in1.toString().equals("-1"));
		
		// Test 0.
		final IntB i0 = I(0);
		
		ASSERT(i0.eq(IntB.ZERO));
		ASSERT(i0.eq(i0.zero()));
		ASSERT(i0.eq(0));
		ASSERT(i0.toString().equals("0"));
		
		// Test 1.		
		final IntB i1 = I(1);
		
		ASSERT(i1.eq(IntB.ONE));
		ASSERT(i1.eq(i1.one()));
		ASSERT(i1.eq(1));
		ASSERT(i1.toString().equals("1"));
	
		// Test 2.
		final IntB i2 = I(2);
		ASSERT(i2.toString().equals("2"));
		
		
		// -- Test for proper compare.
		ASSERT(i2.compareTo(in2) > 0);
		ASSERT(in2.compareTo(i2) < 0);
		ASSERT(i1.compareTo(in2) > 0);
		ASSERT(in1.compareTo(i2) < 0);
		ASSERT(i2.compareTo(i2) == 0);
		ASSERT(in2.compareTo(in2) == 0);
		
		// -- Test Addition.
		
		// - Additive identity.
		ASSERT(i0.equals(i0.add(i0)));
		ASSERT(i1.equals(i1.add(i0)));
		ASSERT(i1.equals(i0.add(i1)));
		
		// negative and negative.
		IntB result = in1.add(in1);
		ASSERT(result.equals(in2));
		ASSERT(result.toString().equals("-2"));
		
		// negative and positive.
		result = in2.add(i1);
		ASSERT(result.equals(in1));
		ASSERT(result.toString().equals("-1"));
		
		// Positive and negative.
		result = i2.add(in1);
		ASSERT(result.equals(i1));
		ASSERT(result.toString().equals("1"));
		
		// Positive and positive.
		result = i1.add(i1);
		ASSERT(result.equals(i2));
		ASSERT(result.toString().equals("2"));
		
		// - Bigger addition tests.
		result = i0;
		for(int i = 0; i <= TEST_SIZE; i++)
		{
			result = result.add(I(i));
		}
		ASSERT(result.toString().equals("50005000"));
		
		result = i0;
		for(int i = 0; i <= TEST_SIZE; i++)
		{
			result = result.add(I(-i));
		}
		ASSERT(result.toString().equals("-50005000"));
		
		result = i0;
		for(int i = 0; i <= TEST_SIZE; i++)
		{
			result = result.add(I((int)(Math.pow(-1, i)*i)));
		}

		ASSERT(result.toString().equals("5000"));
		
		// -- Test subtraction.
		
		// Big Positive - small positive 
		result = i2.sub(i1);
		ASSERT(result.eq(i1));
		
		// Big Positive - large positive.
		result = i1.sub(i2);
		ASSERT(result.eq(in1));
		
		// positive - negative.
		result = i1.sub(in1);
		ASSERT(result.eq(i2));
		
		// negative - positive.
		result = in1.sub(i1);
		ASSERT(result.eq(in2));
		
		// negative - large negative.
		result = in1.sub(in2);
		ASSERT(result.eq(i1));
		
		// negative - small negative.
		result = in2.sub(in1);
		ASSERT(result.eq(in1));
		
		// equal subtraction.
		result = i2.sub(i2);
		ASSERT(result.eq(i0));
		
		result = in2.sub(in2);
		ASSERT(result.eq(i0));
		
		// Large negative doubling subtraction test.
		result = in1;
		for(int i = 0; i < 100; i++)
		{
			result = result.sub(result.neg());
		}
		
		ASSERT(result.toString().equals("-1267650600228229401496703205376"));
		
		// 0 - positive.
		result = i0.sub(i1);
		ASSERT(result.toString().equals("-1"));
		
		// 0 - negative
		result = i0.sub(in1);
		ASSERT(result.toString().equals("1"));
		
		// positive - 0.
		result = i1.sub(i0);
		ASSERT(result.toString().equals("1"));
		
		// negative - 0.
		result = in1.sub(i0);
		ASSERT(result.toString().equals("-1"));
		
		// More subtraction testing.
		result = I(172).sub(252);
		ASSERT(result.eq(-80));
		
		// -- Test Multiplication.
		
		// Multiplicative identity.
		ASSERT(in2.mult(i1).eq(in2));
		ASSERT(in1.mult(i1).eq(in1));
		ASSERT(i0 .mult(i1).eq(i0 ));
		ASSERT(i1 .mult(i1).eq(i1 ));
		ASSERT(i2 .mult(i1).eq(i2 ));

		// Symmetric property.
		ASSERT(i1.mult(in2).eq(in2));
		ASSERT(i1.mult(in1).eq(in1));
		ASSERT(i1.mult(i0 ).eq(i0 ));
		ASSERT(i1.mult(i1 ).eq(i1 ));
		ASSERT(i1.mult(i2 ).eq(i2 ));
		
		// Test negation.
		ASSERT(in2.eq(i2 .mult(in1)));
		ASSERT(in1.eq(i1 .mult(in1)));
		ASSERT(i2 .eq(in2.mult(in1)));
		
		// Symmetric property.
		ASSERT(in2.eq(in1.mult(i2)));
		ASSERT(in1.eq(in1.mult(i1)));
		ASSERT(i2 .eq(in1.mult(in2)));
		
		// TEST more potent numbers.
		result = i2.mult(i2);
		ASSERT(result.toString().equals("4"));
		ASSERT(result.eq(4));
		
		// Test large multiplication by 2s.
		result = i1;
		for(int i = 0; i < 100; i++)
		{
			result = result.mult(i2);
		}
		
		ASSERT(result.toString().equals("1267650600228229401496703205376"));
		
		// Test large differing multiplication.
		
		result = i1;
		
		for(int i = 1; i <= 50; i++)
		{
			result = result.mult(I(i));
		}
		
		ASSERT(result.toString().equals("30414093201713378043612608166064768844377641568960512000000000000"));

		// Test large negative times positive.
		result = I(-56786).mult(23798798);
		ASSERT(result.toString().equals("-1351438543228"));
		
		// Symmetric.
		result = I(23798798).mult(-56786);
		ASSERT(result.toString().equals("-1351438543228"));
		
		result = I(-43278).mult(I(-9876543));

		ASSERT(result.toString().equals("427437027954"));
		
		// -- Test Integer division.
		
		boolean success = true;
		
		// Test for divide by zero errors.
		try
		{
			i0.div(i0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			I(59437).div(i0);
			success = false;
		}
		catch(Error e){}
		
		// As it should be.
		ASSERT(success);
		

		// Test for division by the multiplicative
		// inverse of the multiplicative identity.
		ASSERT(I(89898989).div(i1).eq(I(89898989)));
		
		// Test division of equal numbers.
		ASSERT(I(4327983).div(I(4327983)).eq(i1));
		
		// Test more complex division.
		result = I(432349873).div(98732);
		ASSERT(result.toString().equals("4379"));
		
		result = I(81).div(9);
		ASSERT(result.toString().equals("9"));
		
		result = I(81).div(9);
		ASSERT(result.toString().equals("9"));
		
		result = I(373030).div(23780);
		ASSERT(result.toString().equals("15"));
		
		result = I(2468).div(2);
		ASSERT(result.eq(1234));
		
		// Test negatives.
		
		result = I(144).div(-12);
		ASSERT(result.eq(-12));
		
		result = I(-347).div(10);
		ASSERT(result.eq(-34));
		
		// Test Modular division.
		
		result = I(123456789).div(123456790);
		ASSERT(result.toString().equals("0"));
		
		result = I(123456789).mod(123456790);
		ASSERT(result.toString().equals("123456789"));
		
		result = I(56).mod(17);
		ASSERT(result.toString().equals("5"));
		
		// Test for modular arithmetic involving negative numbers.
		
		result = I(78).mod(67);
		ASSERT(result.eq(11));
		
		result = I(-78).mod(67);
		ASSERT(result.eq(56));
		
		result = I(78).mod(-67);
		ASSERT(result.eq(-56));
		
		result = I(-78).mod(-67);
		ASSERT(result.eq(-11));
		
		success = true;
		
		// Test for divide by zero errors.
		try
		{
			i0.mod(i0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			I(59437).mod(i0);
			success = false;
		}
		catch(Error e){}
		
		// As it should be.
		ASSERT(success);
		
		// more test mod.
		
		result = I(345).mod(252);
		ASSERT(result.eq(93));
				
		// -- Test toInt().
		
		int max = IntB.MAX_FINITE_INT_VALUE;
		int min = IntB.MIN_FINITE_INT_VALUE;
		
		IntB MAX = I(max);
		IntB MIN = I(min);
		
		ASSERT(MAX.toInt() ==  max);
		ASSERT(MIN.toInt() == -min);
		
		ASSERT(in2.toInt() == -2);
		ASSERT(in1.toInt() == -1);
		ASSERT(i0 .toInt() ==  0);
		ASSERT(i1 .toInt() ==  1);
		ASSERT(i2 .toInt() ==  2);
		
		ASSERT(I( 324798).toInt() ==  324798);
		ASSERT(I(-324798).toInt() == -324798);
		
		// -- Test toLong();
		
		long maxL = Long.MAX_VALUE;
		long minL = Long.MIN_VALUE;

		IntB MAXL = I(maxL);
		IntB MINL = I(minL);
		
		ASSERT(MAXL.toLong() ==  maxL);
		ASSERT(MINL.toLong() == -minL);
		
		ASSERT(in2.toLong() == -2l);
		ASSERT(in1.toLong() == -1l);
		ASSERT(i0 .toLong() ==  0l);
		ASSERT(i1 .toLong() ==  1l);
		ASSERT(i2 .toLong() ==  2l);
		
		ASSERT(I( 324798005600l).toLong() ==  324798005600l);
		ASSERT(I(-324798005600l).toLong() == -324798005600l);
		
		// -- Test pow.
		
		// 2 ^ 100
		result = I(2).pow(100);
		ASSERT(result.toString().equals("1267650600228229401496703205376"));
		
		// 67 ^ 87
		result = I(67).pow(87);
		ASSERT(result.toString().equals("738767590750132809484087434974544164876448944195922356184303556933534287692449121264386896790090535068380171690486648774057318149756968018646121051563466528523"));
		
		// -- Speed of power comparison.
		
		// Mult.
		/*
		result = Integer.ONE;
		for(int i = 0 ; i < 200; i++)
		{
			result = result.mult(89);
		}
		//*/
		
		/*
		// Exponention.
		result = I(89).pow(2000);
		//*/
		
		// NOTE : the mult performs many multiplications involving the little 89,
		// whereas the exponentiation performs fewer multiplications involving the squaring of numbers.
		
		
		// -- Test Boolean operators.
		
		// - & (and)
		result = I(4).AND(3);
		ASSERT(result.eq(0));
		
		result = I(24390).AND(34827);
		ASSERT(result.eq(2050));
		
		ASSERT(I(99999999).AND(0).eq(0));
		
		// negatives.
		ASSERT(I(678).AND(45).eq(36));
		ASSERT(I(-678).AND(45).eq(-36));
		ASSERT(I(678).AND(-45).eq(-36));
		ASSERT(I(-678).AND(-45).eq(-36));
		
		// - | (OR)
		result = I(63).OR(0);
		ASSERT(result.eq(63));

		result = I(2523).OR(388837);
		ASSERT(result.eq(389119));
		
		// Negatives
		ASSERT(I(678).OR(45).eq(687));
		ASSERT(I(-678).OR(45).eq(687));
		ASSERT(I(678).OR(-45).eq(687));
		ASSERT(I(-678).OR(-45).eq(-687));
		
		// - ^ (xor)

		ASSERT(I(567) .XOR(79) .eq(-632));
		ASSERT(I(-567).XOR(79) .eq(632));
		ASSERT(I(-567).XOR(-79).eq(-632));
		ASSERT(I(567) .XOR(-79).eq(632));
	
		// -- Test from string constructor.
		
		String input = "6152348723894723984729834723852375902843509238";
		
		result = new IntB(input);
		ASSERT(result.toString().equals(input));
		
		// -- 0.
		input = "0";
		
		result = new IntB(input);
		ASSERT(result.toString().equals(input));
		
		
		// -- Negative.
		input = "-34082740";
		
		result = new IntB(input);
		ASSERT(result.toString().equals(input));		
		
	}
	
	private void test_Rationals()
	{
		// -- Test Zero and equality.
		Rational result = Rational.ZERO;
		ASSERT(result.toString().equals("0"));
		ASSERT(result.eq(0));
		ASSERT(result.equals(result));
		ASSERT(result.eq(Rational.ZERO));
		ASSERT(result.eq(R(0)));
		
		// Test initialization and simplification.
		result = R(9834496, 56*56);
		ASSERT(result.eq(R(3136)));
		
		result = R(3, -4);
		ASSERT(result.eq(R(-3, 4)));
		
		result = R(-3, -4);
		ASSERT(result.eq(R(3, 4)));		
		
		// -- Test Addition.
		result = R(345, 597).add(R(43788, 23));
		ASSERT(result.eq(R(8716457, 4577)));

		result = R(I(27598237), I(1238762178)).add(R(I("1793264197326"), I("4327462378") ));
		ASSERT(result.equals(R(I("1110773646570647081807"), I("2680348360292169642"))));
		
		
		// -- Test Subtraction.
		result = R("27598237/1238762178").sub(R("1793264197326/4327462378") );
		ASSERT(result.equals(R("-1110654216238330454221/2680348360292169642")));
		
		result = R(1,4).sub(R(-3, 4));
		ASSERT(result.eq(Rational.ONE));
		
		
		// -- Test Multiplication.
		result = R("46328794/48793210941379").mult(R("347623784687321648/83741984798"));
		ASSERT(result.eq(R("8052495355139639520966256/2043020164449283743578221")));
		
		result = R(3, -4).mult(R(-1, -2));
		ASSERT(result.eq(R(-3, 8)));

		
		// -- Test Division.
		result = R("349824792/47239429849832").div(R("213678213612876/90909090909090"));
		ASSERT(result.eq(R("73616328282827546665/23365826348154798423290826")));
		
		Rational r0 = Rational.ZERO;

		
		// -- Test for errors.
		
		boolean success = true;
		
		// Test for divide by zero errors.
		try
		{
			r0.mod(r0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			R(59437).mod(r0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			R(59467).div(r0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			r0.div(r0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			result = R("14524176867349327498/0");
			success = false;
		}
		catch(Error e){}
		
		// As it should be.
		ASSERT(success);
		
		// -- Test Part functions.
		result = R(45, 4);
		ASSERT(result.part_int().eq(11));
		ASSERT(result.part_frac().eq(R(1,4)));
		
		result = R(-389, 23);
		ASSERT(result.part_int() .eq(-16));
		ASSERT(result.part_frac().eq(R(-21, 23)));
		
		
		// Test string parsing.
		
		result = R(".45");
		ASSERT(result.eq(R(45, 100)));
		
		result = R("45525");
		ASSERT(result.eq(R(45525)));
		
		result = R("143.234");
		ASSERT(result.eq(R(143234, 1000)));
		
		result = R("1.56/23.56");
		ASSERT(result.eq(R(39, 589)));
	}
	
	private void test_Complex()
	{
		Complex c1, c2, c3;
		
		c1 = C(0, 0);
		
		ASSERT(c1.toString().equals("0"));
		ASSERT(c1.eq(Complex.ZERO));
		ASSERT(c1.equals(Complex.ZERO));
		ASSERT(c1.eq(0));
		
		c1 = C(1, 0);
		
		ASSERT(c1.toString().equals("1"));
		ASSERT(c1.eq(Complex.ONE));
		ASSERT(c1.equals(Complex.ONE));
		ASSERT(c1.eq(1));
		
		c1 = C(R(5,6), R(-7,8));
		ASSERT(c1.part_real().toString().equals("\\frac{5}{6}"));
		ASSERT(c1.part_imaginary().toString().equals("\\frac{-7}{8}"));
	
		
		// Test Addition.
		
		c1 = C(R(5,6), R(8));
		c2 = C(R(-74, 893), R(1, -2));
		c3 = c1.add(c2);
		
		ASSERT(c3.eq(C(R(4021, 5358), R(15, 2))));
		
		// Test subtraction.
		c3 = c1.sub(c2);
		ASSERT(c3.eq(C(R(4909, 5358), R(17, 2))));
		
		// Test Multiplication.
		c3 = c1.mult(c2);
		ASSERT(c3.eq(C(R(10531, 2679), R(-11569, 10716))));

		c3 = C(1, 5).mult(-1);
		ASSERT(c3.eq(C(-1, -5)));
		c3 = c3.neg();
		ASSERT(c3.eq(C(1, 5)));
		
		// Test Division.
		c3 = c1.div(c2);
		ASSERT(c3.eq(C(R(-38938372, 2458059), R(-2356627, 2458059))));
		
		c1 = C(R(56, 1), R(5,3));
		c2 = c1.conj();
		
		ASSERT(c2.eq(C(R(56, 1), R(-5, 3))));
		
		ASSERT(c1.part_real().eq(R(56, 1)));
		ASSERT(c1.part_imaginary().eq(R(5, 3)));
		
		c1 = C(5, 5);
		c2 = Complex.I;
		c3 = c1.mult(c2);
		ASSERT(c3.eq(C(-5, 5)));
		
	}
	
	private void test_Multinomials()
	{
		Multinomial m1, m2, m3;

		m1 = new Multinomial(0);
		ASSERT(m1.toString().equals("0"));
		
		m1 = new Multinomial(1);
		ASSERT(m1.toString().equals("1"));
		
		ASSERT(Multinomial.ONE .toString().equals("1"));
		ASSERT(Multinomial.ZERO.toString().equals("0"));
		
		m1 = new Multinomial(385729);
		ASSERT(m1.toString().equals("385729"));
		
		m1 = new Multinomial(5, "X");
		ASSERT(m1.toString().equals("5X"));
		
		m2 = new Multinomial(-6, "Y");
		ASSERT(m2.toString().equals("-6Y"));
		
		m3 = m1.add(m2);
		ASSERT(m3.toString().equals("5X - 6Y"));
		
		m3 = m3.add(m3);
		ASSERT(m3.toString().equals("10X - 12Y"));
		
		m1 = new Multinomial(-10, "X");
		m3 = m3.add(m1);
		ASSERT(m3.toString().equals("-12Y"));
		
		m2 = new Multinomial(12, "Y");
		m3 = m3.add(m2);
		ASSERT(m3.toString().equals("0"));
		
		m3 = m1.sub(m2);
		ASSERT(m3.toString().equals("-10X - 12Y"));
		
		m3 = m3.mult(2);
		ASSERT(m3.toString().equals("-20X - 24Y"));
		
		m3 = m3.div(4);
		ASSERT(m3.toString().equals("-5X - 6Y"));

		
		// -- Test multiplication.
		m1 = M(0);
		m1 = m1.mult(1);
		ASSERT(m1.toString().equals("0"));
		
		m1 = M(5).add(M("X"));
		
		m1 = m1.mult(56);
		ASSERT(m1.eq(M(280).add(M(56, "X"))));
		
		m2 = M(67, "Y").add(M(34));
		String s1 = m1.mult(m2).mult(M("X")).add(M(5)).toString();
		String s2 = "3752X^{2}Y + 1904X^{2} + 18760XY + 9520X + 5";
		ASSERT(s1.equals(s2));
		
		boolean success = true;
		
		// -- Test Division.
		// Test for divide by zero errors.
		try
		{
			m1.div(0);
			success = false;
		}
		catch(Error e){}
		
		try
		{
			m1.div(M(0, "E"));
			success = false;
		}
		catch(Error e){}
		
		try
		{
			m1.div(M(1).sub(M(1)));
			success = false;
		}
		catch(Error e){}
		
		try
		{
			m1.div(0);
			success = false;
		}
		catch(Error e){}
		
		// As it should be.
		ASSERT(success);
		
		
		// -- Test Actual division.
		
		// First test simple division by scalars.
		m1 = M(5,"X").add(M(10));
		m1 = m1.div(2);
		ASSERT(m1.equals(M(R(5,2), "X").add(M(5))));
		
		m2 = m1.div(M("X"));
		ASSERT(m2.eq(M(R(5, 2))));
		
		m1 = M("Y").mult(M("X").add(M(1)));
		m2 = M("X").add(M(4));
		m1 = m1.mult(m2);
		ASSERT(m1.toString().equals("X^{2}Y + 5XY + 4Y"));
		
		m1 = m1.div(m2);
		ASSERT(m1.toString().equals("XY + Y"));
	
		// Ensure that the Printin and math work correctly.
		Complex c1 = C(5, 5).mult(Complex.I);
		m1 = M(c1);
		ASSERT(m1.toString().equals("-5 + 5i"));

		
		// -- Test Mod.
		
		
		// FIXME : Test factories.
		throw new Error("You should test Multinomials more!");
	}
	
	/*
	// FIXME : Extend this testing.
	private void test_RationalMultinomials()
	{
		Multinomial m1, m2, m3;
		RationalMultinomial e1, e2;
		
		// -- First test Rational Expressions with trivial denominators.
		
		e1 = RM(M(0), M(1));
		ASSERT(e1.eq(0));
		ASSERT(e1.eq(RationalMultinomial.ZERO));
		
		e1 = RM(M(1), M(1));
		ASSERT(e1.eq(1));
		ASSERT(e1.eq(RationalMultinomial.ONE));

		// Test progressively more complicated expressions.
		e1 = RM(M(5), M(4));
		ASSERT(e1.toString().equals(R(5, 4).toString()));
		
		e1 = RM(M(1), M(R(4, 5)));
		ASSERT(e1.toString().equals(R(5, 4).toString()));
		
		
		
	}
	*/
	
	private void test_Expressions()
	{
		Expression e1, e2;
		
		// Multiplicative identity.
		e1 = new Expression(0);
		ASSERT(e1.toString().equals("0"));
		ASSERT(e1.eq(0));
		ASSERT(e1.eq(Expression.ZERO));

		// Additive identity.
		e1 = new Expression(1);
		ASSERT(e1.toString().equals("1"));
		ASSERT(e1.eq(1));
		ASSERT(e1.eq(Expression.ONE));
		
		// Constants
		e1 = E(56);
		ASSERT(e1.toString().equals("56"));
		ASSERT(e1.eq(56));
		
		Expression e3 = ExpressionFactory.createExpression("56");
		ASSERT(e3.toString().equals("56"));
		ASSERT(e1.toString().equals("56"));
		ASSERT(e1.eq(56));
		
		e1 = ExpressionFactory.createExpression("-100000000000000000");

		ASSERT(e1.toString().equals("-100000000000000000"));
		

		e1 = E(1);
		e2 = E(1);
		e3 = e1.add(e2);
		
		ASSERT(e3.eq(2));
		
		// Test Alebraic Multiplication.
		e1 = ExpressionFactory.createExpression("x^{3} + yhj + 89");
		e2 = ExpressionFactory.createExpression("x+6");
	
		ASSERT(e1.toString().equals("hjy + x^{3} + 89"));
		ASSERT(e2.toString().equals("x + 6"));
		
		e1 = e1.mult(e2);
		
		ASSERT(e1.toString().equals("hjxy + x^{4} + 6hjy + 6x^{3} + 89x + 534"));

		// Test Complex numbers
		e1 = ExpressionFactory.createExpression("6i + 9");
		e2 = ExpressionFactory.createExpression("-9i + 5");
		
		e3 = e1.mult(e2);
		ASSERT(e3.toString().equals("99 - 51i"));
		
		e3 = e1.div(e2);
		ASSERT(e3.toSerialString().equals("(-9 + 111i)/(106)"));
		
		// -- Test a possible degenerate case where coefficients have a non trivial GCF.
		
		e1 = ExpressionFactory.createExpression("(-40h + 100x - 250)/(35x + 300y - 100)");
		ASSERT(e1.toSerialString().equals("(-8h + 20x - 50)/(7x + 60y - 20)"));
		e1 = ExpressionFactory.createExpression("256x/(256y)");
		ASSERT(e1.toSerialString().equals("(x)/(y)"));
		
	}
	
	private void test_Equations()
	{
		Expression exp1, exp2;
		
		exp1 = ExpressionFactory.createExpression("x^2 + 2x + 4");
		exp2 = ExpressionFactory.createExpression("0");
		
		Equation e1, e2, e3;
		
		e1 = Equation.ONE;
		
		ASSERT(e1.toString().equals("1"));
		
		e1 = Equation.ZERO;
		ASSERT(e1.toString().equals("0"));
		
		// Test Equation creation functions.
		e1 = new Equation(exp1);
		ASSERT(e1.toString().equals("x^{2} + 2x + 4"));		
		
		e1 = new Equation(exp1, exp2);
		ASSERT(e1.toString().equals("x^{2} + 2x + 4 = 0"));
		
		e1 = ExpressionFactory.createEquation("x^2 + xy + y^2 = 89i + 78");
		ASSERT(e1.toString().equals("x^{2} + xy + y^{2} = 78 + 89i"));

		e1 = ExpressionFactory.createEquation("");
		ASSERT(e1.eq(0));
		
		// Test Addition.
		e1 = ExpressionFactory.createEquation("2x - 5 -.8h");
		ASSERT(e1.toSerialString().equals("(-4h + 10x - 25)/(5)"));
		e2 = ExpressionFactory.createEquation(".7x + 6y - 2");
		
		e3 = e1.add(e2);
		ASSERT(e3.toSerialString().equals("(-8h + 27x + 60y - 70)/(10)"));
		
		e1 = ExpressionFactory.createEquation("x");
		e2 = ExpressionFactory.createEquation("y = x");
		e3 = e1.add(e2);
		ASSERT(e3.toSerialString().equals("x + y = 2x"));
		e3 = e2.add(e1);
		ASSERT(e3.toSerialString().equals("x + y = 2x"));
		
		// Test Additive Identity Identities.
		ASSERT(e3.equals(e3.add(Equation.ZERO)));
		ASSERT(e3.equals(e3.add(Equation.ZERO_ZERO)));
		
		// -- Test Subtraction.
		
		e3 = e3.sub(e3);
		ASSERT(e3.equals(Equation.ZERO_ZERO));
		
		e1 = ExpressionFactory.createEquation("2x - 5 -.8h");
		e2 = ExpressionFactory.createEquation(".7x + 6y - 2");
		
		e3 = e1.sub(e2);
		ASSERT(e3.toSerialString().equals("(-8h + 13x - 60y - 30)/(10)"));
		e3 = e2.sub(e1);
		ASSERT(e3.toSerialString().equals("(8h - 13x + 60y + 30)/(10)"));
		
		e1 = ExpressionFactory.createEquation("x + 1");
		e2 = ExpressionFactory.createEquation("y = x");
		e3 = e1.sub(e2);
		ASSERT(e3.toSerialString().equals("x - y + 1 = 1"));
		e3 = e2.sub(e1);

		ASSERT(e3.toSerialString().equals("-x + y - 1 = -1"));
		
		ASSERT(e3.equals(e3.sub(Equation.ZERO)));
		
		// -- Test Multiplication
		e1 = ExpressionFactory.createEquation("2x - 5 -.8h");
		e2 = ExpressionFactory.createEquation(".7x + 6y - 2");
		
		e3 = e1.mult(e2);
		ASSERT(e3.toSerialString().equals("(-28hx - 240hy + 70x^{2} + 600xy + 80h - 375x - 1500y + 500)/(50)"));
		e3 = e2.mult(e1);
		ASSERT(e3.toSerialString().equals("(-28hx - 240hy + 70x^{2} + 600xy + 80h - 375x - 1500y + 500)/(50)"));
		
		e1 = ExpressionFactory.createEquation("x + 1");
		e2 = ExpressionFactory.createEquation("y = x");
		e3 = e1.mult(e2);
		ASSERT(e3.toSerialString().equals("xy + y = x^{2} + x"));
		e3 = e2.mult(e1);
		ASSERT(e3.toSerialString().equals("xy + y = x^{2} + x"));
		
		// Test identity.
		ASSERT(e3.eq(e3.mult(Equation.ONE)));
		
		// -- Test Division.
		e1 = ExpressionFactory.createEquation("2x - 5 -.8h");
		e2 = ExpressionFactory.createEquation(".7x + 6y - 2");
		e3 = e2.div(e1);
		ASSERT(e3.toSerialString().equals("(-7x - 60y + 20)/(8h - 20x + 50)"));
		e3 = e1.div(e2);
		ASSERT(e3.toSerialString().equals("(-8h + 20x - 50)/(7x + 60y - 20)"));
				
		e1 = ExpressionFactory.createEquation("x + 1");
		e2 = ExpressionFactory.createEquation("y = x");
		
		// -- Test some more stuff.
		
		throw new Error("You should find some more things to test regarding equations...");
		
	}
	
	private void test_conj()
	{
		IntB n1 = new IntB(78);
		ASSERT(n1.conj().eq(78));
		
		Rational n2 = new Rational(78, 24);
		ASSERT(n2.conj().eq(n2));
		
		Complex n3 = new Complex(n2, n2);
		ASSERT(n3.conj().eq(new Complex(n2, n2.neg())));
		
		//Multinomial n3 = 
	}
	
}