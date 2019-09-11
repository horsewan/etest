package com.eningqu.common.kit;

import com.eningqu.common.base.vo.ValidJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @desc TODO
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/5/4 10:40
 **/
public class ValidationKit {

    private final static Logger logger = LoggerFactory.getLogger(ValidationKit.class);

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidJson validate(T obj, Class... clazz) {
        ValidJson result = new ValidJson();
        Set<ConstraintViolation<T>> set = validator.validate(obj, clazz);
        if (!set.isEmpty()) {
            result.setHasErrors(true);
            for (ConstraintViolation<T> cv : set) {
                result.setMessage(cv.getMessage());
                break;
            }
        }
        return result;
    }

    public static <T> ValidJson validate(T obj) {
        ValidJson result = new ValidJson();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (!set.isEmpty()) {
            result.setHasErrors(true);
            for (ConstraintViolation<T> cv : set) {
                result.setMessage(cv.getMessage());
                break;
            }
        }
        return result;
    }

    public static <T> ValidJson validate(T obj, String propertyName) {
        ValidJson result = new ValidJson();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (!set.isEmpty()) {
            result.setHasErrors(true);
            for (ConstraintViolation<T> cv : set) {
                result.setMessage(cv.getMessage());
            }
        }
        return result;
    }

    public interface AddValid{}
    public interface EditValid{}
    public interface GroupValid{}
}
