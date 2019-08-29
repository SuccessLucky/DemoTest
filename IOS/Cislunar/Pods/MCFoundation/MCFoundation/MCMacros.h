//
//  MCMacros.h
//  MCLogger
//
//  Created by mylcode on 2017/11/9.
//  Copyright © 2017年 MC-Studio. All rights reserved.
//

#define keypath(...) \
_Pragma("clang diagnostic push") \
_Pragma("clang diagnostic ignored \"-Warc-repeated-use-of-weak\"") \
macro_if_eq(1, macro_argcount(__VA_ARGS__))((keypath1(__VA_ARGS__)))((keypath2(__VA_ARGS__))) \
_Pragma("clang diagnostic pop") \

#define keypath1(PATH) \
((void)(NO && ((void)PATH, NO)), strchr(# PATH, '.') + 1)

#define keypath2(OBJ, PATH) \
((void)(NO && ((void)OBJ.PATH, NO)), # PATH)

/**
 * If A is equal to B, the next argument list is expanded; otherwise, the
 * argument list after that is expanded. A and B must be numbers between one
 * and nine, inclusive. 
 *
 * @code
 // expands to true
 macro_if_eq(0, 0)(true)(false)
 // expands to false
 macro_if_eq(0, 1)(true)(false)
 * @endcode
 *
 * This is primarily useful when dealing with indexes and counts in
 * metaprogramming.
 */
#define macro_at(N, ...) macro_concat(macro_at, N)(__VA_ARGS__)

#define macro_head(FIRST, ...) FIRST

#define macro_if_eq(A, B) macro_concat(macro_if_eq, A)(B)

//A、B:[0~9]
#define macro_less_eq(A, B)     macro_concat(macro_less_eq, A)(B)
#define macro_greater_eq(A, B)  macro_concat(_macro_greater_eq, A)(B)

#define macro_argcount(...) \
macro_at(9, __VA_ARGS__, 9, 8, 7, 6, 5, 4, 3, 2, 1)

#define macro_concat(A, B) A ## B

#define macro_dec(VAL) macro_at(VAL, m1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

#define macro_consume(...)

#define macro_expand(...) __VA_ARGS__

#define macro_if_eq0_m1(...) macro_expand
#define macro_if_eq0_0(...) __VA_ARGS__ macro_consume
#define macro_if_eq0_1(...) macro_expand
#define macro_if_eq0_2(...) macro_expand
#define macro_if_eq0_3(...) macro_expand
#define macro_if_eq0_4(...) macro_expand
#define macro_if_eq0_5(...) macro_expand
#define macro_if_eq0_6(...) macro_expand
#define macro_if_eq0_7(...) macro_expand
#define macro_if_eq0_8(...) macro_expand
#define macro_if_eq0_9(...) macro_expand

#define macro_if_eq0(VALUE) macro_concat(macro_if_eq0_, VALUE)
#define macro_if_eq1(VALUE) macro_if_eq0(macro_dec(VALUE))
#define macro_if_eq2(VALUE) macro_if_eq1(macro_dec(VALUE))
#define macro_if_eq3(VALUE) macro_if_eq2(macro_dec(VALUE))
#define macro_if_eq4(VALUE) macro_if_eq3(macro_dec(VALUE))
#define macro_if_eq5(VALUE) macro_if_eq4(macro_dec(VALUE))
#define macro_if_eq6(VALUE) macro_if_eq5(macro_dec(VALUE))
#define macro_if_eq7(VALUE) macro_if_eq6(macro_dec(VALUE))
#define macro_if_eq8(VALUE) macro_if_eq7(macro_dec(VALUE))
#define macro_if_eq9(VALUE) macro_if_eq8(macro_dec(VALUE))

#define _macro_less_eq0_m1(...) macro_expand
#define _macro_less_eq0_0(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_1(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_2(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_3(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_4(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_5(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_6(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_7(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_8(...) __VA_ARGS__ macro_consume
#define _macro_less_eq0_9(...) __VA_ARGS__ macro_consume

#define macro_less_eq0(VALUE) macro_concat(_macro_less_eq0_, VALUE)
#define macro_less_eq1(VALUE) macro_less_eq0(macro_dec(VALUE))
#define macro_less_eq2(VALUE) macro_less_eq1(macro_dec(VALUE))
#define macro_less_eq3(VALUE) macro_less_eq2(macro_dec(VALUE))
#define macro_less_eq4(VALUE) macro_less_eq3(macro_dec(VALUE))
#define macro_less_eq5(VALUE) macro_less_eq4(macro_dec(VALUE))
#define macro_less_eq6(VALUE) macro_less_eq5(macro_dec(VALUE))
#define macro_less_eq7(VALUE) macro_less_eq6(macro_dec(VALUE))
#define macro_less_eq8(VALUE) macro_less_eq7(macro_dec(VALUE))
#define macro_less_eq9(VALUE) macro_less_eq8(macro_dec(VALUE))

#define _macro_greater_eq0_m1(...) __VA_ARGS__ macro_consume
#define _macro_greater_eq0_0(...) __VA_ARGS__ macro_consume
#define _macro_greater_eq0_1(...) macro_expand
#define _macro_greater_eq0_2(...) macro_expand
#define _macro_greater_eq0_3(...) macro_expand
#define _macro_greater_eq0_4(...) macro_expand
#define _macro_greater_eq0_5(...) macro_expand
#define _macro_greater_eq0_6(...) macro_expand
#define _macro_greater_eq0_7(...) macro_expand
#define _macro_greater_eq0_8(...) macro_expand
#define _macro_greater_eq0_9(...) macro_expand

#define _macro_greater_eq0(VALUE) macro_concat(_macro_greater_eq0_, VALUE)
#define _macro_greater_eq1(VALUE) _macro_greater_eq0(macro_dec(VALUE))
#define _macro_greater_eq2(VALUE) _macro_greater_eq1(macro_dec(VALUE))
#define _macro_greater_eq3(VALUE) _macro_greater_eq2(macro_dec(VALUE))
#define _macro_greater_eq4(VALUE) _macro_greater_eq3(macro_dec(VALUE))
#define _macro_greater_eq5(VALUE) _macro_greater_eq4(macro_dec(VALUE))
#define _macro_greater_eq6(VALUE) _macro_greater_eq5(macro_dec(VALUE))
#define _macro_greater_eq7(VALUE) _macro_greater_eq6(macro_dec(VALUE))
#define _macro_greater_eq8(VALUE) _macro_greater_eq7(macro_dec(VALUE))
#define _macro_greater_eq9(VALUE) _macro_greater_eq8(macro_dec(VALUE))

#define macro_atm1(...) macro_head(__VA_ARGS__)
#define macro_at0(...) macro_head(__VA_ARGS__)
#define macro_at1(_0, ...) macro_head(__VA_ARGS__)
#define macro_at2(_0, _1, ...) macro_head(__VA_ARGS__)
#define macro_at3(_0, _1, _2, ...) macro_head(__VA_ARGS__)
#define macro_at4(_0, _1, _2, _3, ...) macro_head(__VA_ARGS__)
#define macro_at5(_0, _1, _2, _3, _4, ...) macro_head(__VA_ARGS__)
#define macro_at6(_0, _1, _2, _3, _4, _5, ...) macro_head(__VA_ARGS__)
#define macro_at7(_0, _1, _2, _3, _4, _5, _6, ...) macro_head(__VA_ARGS__)
#define macro_at8(_0, _1, _2, _3, _4, _5, _6, _7, ...) macro_head(__VA_ARGS__)
#define macro_at9(_0, _1, _2, _3, _4, _5, _6, _7, _8, ...) macro_head(__VA_ARGS__)
