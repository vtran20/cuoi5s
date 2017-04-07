package com.easysoft.ecommerce.service.promotion;

import com.easysoft.ecommerce.model.ConditionClass;
import com.easysoft.ecommerce.model.Promotion;
import com.easysoft.ecommerce.model.PromotionCondition;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.session.ItemMap;
import com.easysoft.ecommerce.model.session.SessionObject;
import com.easysoft.ecommerce.service.ServiceLocatorHolder;
import com.easysoft.ecommerce.service.condition.Condition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * User: Vu Tran
 * Date: Dec 19, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AwardSupport implements Award {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwardSupport.class);

    public void init() throws Exception {
        //do something here if need.
    }

    public void destroy() {
        //do something here
    }

//    /**
//     * This method valid whether the promotion condition is valid or not.
//     *
//     * @param sessionObject
//     * @param promotion
//     * @return
//     */
//    public boolean isValidPromoCode(SessionObject sessionObject, Promotion promotion) {
//        if (promotion == null) return false;
//        ExpressionParser parser = new SpelExpressionParser();
//        List<PromotionCondition> promotionConditions = ServiceLocatorHolder.getServiceLocator().getPromotionConditionDao().findBy("promotion.id", promotion.getId());
//        String expression = "";
//        String logical = "";
//        for (PromotionCondition promotionCondition : promotionConditions) {
//            ConditionClass conditionClass = ServiceLocatorHolder.getServiceLocator().getConditionClassDao().findById(promotionCondition.getCondition().getId());
//            if (StringUtils.isEmpty(expression)) {
//                expression = buildExpression(promotionCondition.getExpression(), conditionClass.getClassName(), sessionObject, null, null);
//                logical = promotionCondition.getLogical();
//            } else {
//                if (StringUtils.isEmpty(logical)) {
//                    throw new IllegalArgumentException("Logical is missing.");
//                } else {
//                    String temp = buildExpression(promotionCondition.getExpression(), conditionClass.getClassName(), sessionObject, null, null);
//                    if (!StringUtils.isEmpty(temp)) {
//                        expression += " " + logical + " " + temp;
//                        logical = promotionCondition.getLogical();
//                    }
//                }
//            }
//        }
//        boolean isValid = false;
//        if (!StringUtils.isEmpty(expression)) {
//            isValid = parser.parseExpression(expression).getValue(Boolean.class);
//        }
//        LOGGER.info("Expression=" + expression + " - " + isValid);
//        return isValid;
//    }

    public String buildExpression(String expression, String conditionClass, SessionObject so, ItemMap itemMap, User user) {
        String result = null;

        try {
            if (!StringUtils.isEmpty(expression) && !StringUtils.isEmpty(conditionClass)) {
                Class c = null;
                c = Class.forName(conditionClass);
                Condition condition = (Condition) c.newInstance();
                result = condition.execute(so, itemMap, user, expression);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error("Expression error:" + expression + ":" + conditionClass, e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method valid whether the promotion condition is valid or not.
     *
     * @param item
     * @param promotion
     * @return
     */
    public boolean isValidPromoCode(SessionObject so, ItemMap item, User user, Promotion promotion) {
        if (promotion == null) return false;
        ExpressionParser parser = new SpelExpressionParser();
        List<PromotionCondition> promotionConditions = ServiceLocatorHolder.getServiceLocator().getPromotionConditionDao().findBy("promotion.id", promotion.getId());
        String expression = "";
        String logical = "";
        for (PromotionCondition promotionCondition : promotionConditions) {
            ConditionClass conditionClass = ServiceLocatorHolder.getServiceLocator().getConditionClassDao().findById(promotionCondition.getCondition().getId());
            if (StringUtils.isEmpty(expression)) {
                expression = buildExpression(promotionCondition.getExpression(), conditionClass.getClassName(), so, item, user);
                logical = promotionCondition.getLogical();
            } else {
                if (StringUtils.isEmpty(logical)) {
                    throw new IllegalArgumentException("Logical is missing.");
                } else {
                    String temp = buildExpression(promotionCondition.getExpression(), conditionClass.getClassName(), so, item, user);
                    if (!StringUtils.isEmpty(temp)) {
                        expression += " " + logical + " " + temp;
                        logical = promotionCondition.getLogical();
                    }
                }
            }
        }
        boolean isValid = false;
        if (!StringUtils.isEmpty(expression)) {
            isValid = parser.parseExpression(expression).getValue(Boolean.class);
        }
        LOGGER.info("Expression=" + expression + " - " + isValid);
        return isValid;
    }

}
