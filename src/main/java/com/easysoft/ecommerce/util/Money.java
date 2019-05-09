package com.easysoft.ecommerce.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;

//import com.easysoft.ecommerce.model.Site;
//import com.easysoft.ecommerce.service.ServiceLocatorHolder;

/**
 * Represent an amount of money in any currency.
 *
 * <P>This class assumes <em>decimal currency</em>, without funky divisions
 * like 1/5 and so on. <tt>Money</tt> objects are immutable. Like {@link BigDecimal},
 * many operations return new <tt>Money</tt> objects. In addition, most operations
 * involving more than one <tt>Money</tt> object will throw a
 * <tt>MismatchedCurrencyException</tt> if the currencies don't match.
 *
 * <h2>Decimal Places and Scale</h2>
 * Monetary amounts can be stored in the database in various ways. Let's take the
 * example of dollars. It may appear in the database in the following ways :
 * <ul>
 *  <li>as <tt>123456.78</tt>, with the usual number of decimal places
 *    associated with that currency.
 *  <li>as <tt>123456</tt>, without any decimal places at all.
 *  <li>as <tt>123</tt>, in units of thousands of dollars.
 *  <li>in some other unit, such as millions or billions of dollars.
 * </ul>
 *
 * <P>The number of decimal places or style of units is referred to as the
 * <em>scale</em> by {@link java.math.BigDecimal}. This class's constructors
 * take a <tt>BigDecimal</tt>, so you need to understand it use of the idea of scale.
 *
 * <P>The scale can be negative. Using the above examples :
 * <table border='1' cellspacing='0' cellpadding='3'>
 *  <tr><th>Number</th><th>Scale</th></tr>
 *  <tr><td>123456.78</th><th>2</th></tr>
 *  <tr><td>123456</th><th>0</th></tr>
 *  <tr><td>123 (thousands)</th><th>-3</th></tr>
 * </table>
 *
 * <P>Note that scale and rounding are two separate issues.
 * In addition, rounding is only necessary for multiplication and division operations.
 * It doesn't apply to addition and subtraction.
 *
 * <h2>Operations and Scale</h2>
 * <P>Operations can be performed on items having <em>different scale</em>.
 * For example, these  operations are valid (using an <em>ad hoc</em>
 * symbolic notation):
 * <PRE>
 * 10.plus(1.23) => 11.23
 * 10.minus(1.23) => 8.77
 * </PRE>
 *
 * <h2>Multiplication, Division and Extra Decimal Places</h2>
 * <P>Operations involving multiplication and division are different, since the result
 * can have a scale which exceeds that expected for the given currency. For example
 * <PRE>($10.00).times(0.1256) => $1.256</PRE>
 * which has more than two decimals. In such cases, <em>this class will always round
 * to the expected number of decimal places for that currency.</em>
 * This is the simplest policy, and likely conforms to the expectations of most
 * end users.
 *
 * <P>This class takes either an <tt>int</tt> or a {@link BigDecimal} for its
 * multiplication and division methods. It doesn't take <tt>float</tt> or
 * <tt>double</tt> for those methods, since those types don't interact well with
 * <tt>BigDecimal</tt>. Instead, the <tt>BigDecimal</tt> class must be used when the
 * factor or divisor is a non-integer.
 *
 *
 * User: vtran
 * Date: Aug 4, 2010
 * Time: 11:44:11 AM
 *
 */
public class Money implements Comparable {

    //public static final String CURRENCY = "CURRENCY";
    //public static final String CURRENCY_FORMAT = "CURRENCY_FORMAT";
    
    /**
     * The amount of money in cents.
     */
    private BigDecimal amount = null;

    /**
     * The currency of the money.
     */
    private String currency = "VND";

    /**
     * The currency of the money.
     */
    private String currencyFormat = "#.###.###";

    private void validate(){
      if(StringUtils.isEmpty(currency)) {
        throw new IllegalArgumentException("Currency cannot be null");
      }
      if( StringUtils.isEmpty(currencyFormat)) {
        throw new IllegalArgumentException("Currency Format cannot be null");
      }
      if (amount == null ) {
          throw new IllegalArgumentException("Amount cannot be null");
      }
    }

    /**
     * Constructor with the amount and currency set.
     *
     * @param amount	The money amount.
     * @param currency	The money currency.
     * @param currencyFormat	The money currency format.
     */
    public Money(BigDecimal amount, String currency, String currencyFormat) {
        this.amount = amount;
        this.currency = currency;
        this.currencyFormat = currencyFormat;
        this.validate();
    }
//
//    /**
//     * Constructor with the amount and currency set.
//     *
//     * @param amount	The money amount.
//     * @param site	site object.
//     */
//    public Money(BigDecimal amount, Site site) {
//        this.amount = amount;
//        this.currency = site.getSiteParamsMap().get(CURRENCY);
//        this.currencyFormat = site.getSiteParamsMap().get(CURRENCY_FORMAT);
//        this.validate();
//    }
//
//    /**
//     * Constructor with the amount and currency set.
//     *
//     * @param amount	The money amount.
//     */
//    public Money(BigDecimal amount) {
//        this(amount, ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite());
//    }

    /**
     * Returns the String representation of the amount and currency. Showing currency unit by default
     * Ex: 3.99$
     *     100.000 VND
     * @return The amount and currency in a money format.
     */
    public String toString() {
        return toString(this.currencyFormat);
    }

    /**
     * Returns the String representation of the amount. If noCurrency=true, don't show currency
     * Ex: $3.99     <--> 3.99
     *     100.000 VND <--> 100.000
     * @param format
     * @return The amount and currency in a money format.
     */
    public String toString(String format) {
//        if (this.amount.intValue() <= 0) return "";
        StringBuffer result = new StringBuffer();
        result.append(this.currency).append(getMoneyValue(format));
        return result.toString();
    }
    /**
     * Returns the String representation of the amount. If noCurrency=true, don't show currency
     * Ex: $3.99     <--> 3.99
     *     100.000 VND <--> 100.000
     * @param format
     * @return The amount and currency in a money format.
     */
    public String toStringKeepZero(String format) {
        StringBuffer result = new StringBuffer();

        if (this.amount.intValue() > 0) {
            result.append(this.currency).append(getMoneyValue(format));
        } else {
            result.append(this.currency).append(0);
        }
        return result.toString();
    }
    public String toStringKeepZero() {
        return toStringKeepZero(this.currencyFormat);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     * <p/>
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)<p>
     * <p/>
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.<p>
     * <p/>
     * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.<p>
     * <p/>
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * @param o the Object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *                            from being compared to this Object.
     */
    public int compareTo(Object o) {
        return this.amount.compareTo(getAmount());
    }

//    /**
//     * Converts a valid Money string into a Money object. The input
//     * expects a string that starts with one char as currency, followed
//     * by amount. Normally, we use to convert money from database (cents) to Money object and use for display on the front-end.
//     *
//     * For example:
//     * 100 -> 100 VND & 1.00$
//     * 100.00 -> 10.000 VND & 100.00$
//     *
//     * @param value 	a string to be converted.
//     * @return Money object represented by the supplied string.
//     */
//    public static Money valueOf(String value){
//        return valueOf(value, null, null);
//    }

    public static Money valueOf(String value, String currency, String currencyFormat){
        if (StringUtils.isEmpty(value)) return null;
        //if (StringUtils.isEmpty(currency)) currency = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get(CURRENCY);
        //if (StringUtils.isEmpty(currencyFormat)) currencyFormat = ServiceLocatorHolder.getServiceLocator().getSystemContext().getSite().getSiteParamsMap().get(CURRENCY_FORMAT);
        Money result = null;
        if ( GenericValidator.isLong(value)) {
            result = new Money (new BigDecimal(value), currency, currencyFormat);
        } else if (GenericValidator.isDouble(value)) {
            //value = value.replace(".","");
            result = new Money (new BigDecimal(value), currency, currencyFormat);
        } else {
            throw new IllegalStateException("The string is not money format: "+value);
        }
        return result;
    }

    /**
     * Return money format of money. Don't include currency.
     *
     * For example:
     * 10000 -> 10.000 for VND and 100.00 for USD
     *
     * @return
     */
    public String getMoneyValue () {
        return getMoneyValue(this.currencyFormat);
    }

    public String getMoneyValue (String format) {
//        if (this.amount.intValue() <= 0) return "";
        StringBuffer result = new StringBuffer();
        DecimalFormat precision = new DecimalFormat(format);
        result.append(precision.format(amount).replaceAll(",","."));
        return result.toString();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyFormat() {
        return currencyFormat;
    }

    public String getCurrency() {
        return currency;
    }

    public void checkSameCurrency(Money m){
       if (!this.currency.equals(m.currency))
           throw new IllegalStateException(
                    m.getCurrency() + " doesn't match the expected currency : " + currency);
    }

    /**
     * The value is input from website. Before save to DB, we should call this method to convert to cents.
     *
     * @param value
     * @param currency
     * @return
     */
    public static long moneyToDB (String value, String currency) {
        if (StringUtils.isEmpty(value)) return 0;
        long result = 0;
        if (GenericValidator.isLong(value)) {
            if ("$".equals(currency)) {
                result = Long.valueOf(value)*100;
            } else {
                result = Long.valueOf(value);
            }
        } else if (GenericValidator.isDouble(value)) {
            result = Math.round(Double.valueOf(value)*100);
        }

        return result;
    }
    public Money plus(Money m){
      checkSameCurrency(m);
      return new Money(this.amount.add(m.amount), currency, currencyFormat);
    }

    public Money minus(Money m){
      checkSameCurrency(m);
      return new Money(this.amount.subtract(m.amount), currency, currencyFormat);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;

        Money money = (Money) o;

        if (amount != null ? !amount.equals(money.amount) : money.amount != null) return false;
        if (currency != null ? !currency.equals(money.currency) : money.currency != null) return false;
        if (currencyFormat != null ? !currencyFormat.equals(money.currencyFormat) : money.currencyFormat != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (currencyFormat != null ? currencyFormat.hashCode() : 0);
        return result;
    }

    public static void main (String [] args) {

    }
}
