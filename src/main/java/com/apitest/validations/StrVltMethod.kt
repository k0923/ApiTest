package com.apitest.validations

import org.apache.commons.lang.StringUtils
import java.util.regex.Pattern

enum class StrVltMethod(val method: (left:String?, right:String?) -> Boolean):ICompare<String?> {
    Regex({ left,right -> if (StringUtils.equals(left, right)) true else if (left == null) right == null else Pattern.matches(right, left) }),
    Equals({ left,right -> StringUtils.equals(left,right) }),
    NotEquals({ left,right -> !StringUtils.equals(left,right) }),
    Contains({ left,right -> StringUtils.contains(left,right) }),
    EqualsWithCaseInsensitive({ left,right -> StringUtils.equalsIgnoreCase(left,right) }),
    NotEqualsWithCaseInsensitive({ left,right -> !StringUtils.equalsIgnoreCase(left,right) }),
    ContainsWithCaseInsensitive({ left,right -> StringUtils.containsIgnoreCase(left,right) });

    override fun compare(actual: String?, expect: String?): Boolean {
        return method(actual,expect)
    }

}
