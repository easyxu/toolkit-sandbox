package com.phoenix.common.property.parser.std;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.property.parser.Locator;
import com.phoenix.common.property.parser.ParseHandler;
import com.phoenix.common.property.parser.Parser;

/**
 * 这是一个增强的标准属性文件解析器.
 *
 * <p>
 * 传统型的属性文件被广泛运用于书写系统配置文件以及国际化 (通过<code>java.util.PropertyResourceBundle</code>)领域.
 * 传统型的属性文件可被<code>java.util.Properties</code>读取.
 * </p>
 *
 * <p>
 * 传统的通过<code>java.util.Properties</code>类读取的属性文件有如下缺点:
 *
 * <ul>
 * <li>
 * 只能从InputStream读入(ISO8859-1), 无法读入国际字符(如中文).
 * </li>
 * <li>
 * 属性通过<code>java.util.Properties</code>类读入以后, 它们之间的顺序是不确定的, 不能保持属性在文件中出现的顺序.
 * </li>
 * <li>
 * 难以表现复杂的文本信息.  例如包含多个换行的文本.
 * </li>
 * </ul>
 *
 * 这个增强型的解析器完全兼容传统的属性文件, 但提供了更非富的格式和控制. 它的基本用法如下:
 * <pre>
 *   # 这是注解, 由“#”开始.
 *   ! 这也是注解, 由“!”开始.
 *   # 最简单的定义.  "key"的值为"value", 两端的空白被去掉.
 *   key = value                                # 这里也可以写注解
 *   # 如果一行太长, 可以这样换行.  "longvalue"的值为"aaaa bbbb".
 *   longvalue = aaaa \                         # 这里也可以写注解, 但和反斜杠之间至少要有一个空白.
 *               bbbb
 *   # 数组, 用逗号分隔.
 *   # 这样, "tokens[0]"的值为"first token", "tokens[1]"的值为"second token".
 *   tokens = first token, second token
 *   # 也可以把数组分成多行, 只要前面的key相同, 就被解释成数组.
 *   # 这样的效果同上.
 *   tokens_multiple_lines = first token
 *   tokens_multiple_lines = second token
 *   # 如果出现逗号, 需要用反斜杠转义.
 *   commas.escaped = Hi\, what's up?
 *   # 可以用双引号字符串.
 *   double_quote_string = "Hi, what's up?"     # 这里逗号就不会引起歧义.
 *   # 可以用单引号字符串.
 *   single_quote_string = 'Hi, what\'s up?'    # 这里需要对单引号转义.
 * </pre>
 * 增强的用法如下:
 * <pre>
 *   #!PROPERTIES ENCODING="GBK" UESC           # 在文件的首行加上声明, 可以指定正确的编码方法,
 *                                              # 以及是否使用Java风格的Unicode转义字符.
 *   #!BASE my.document.info
 *   author      = michael                      # 等同于 my.document.info.author
 *   modifyDate  = April 29\, 2001              # 等同于 my.document.info.modifyDate
 *   version     = 1.2.3                        # 等同于 my.document.info.version
 *   #!BASE $base.author
 *   lastname    = Zhou                         # 等同于 my.document.info.author.lastname
 *   firstname   = Michael                      # 等同于 my.document.info.author.firstname
 *   #!BASE $parent                             # 上溯一级, 即 my.document.info 状态
 *   #!BASE $parent.$parent                     # 上溯两级, 即 my 状态
 *   #!BASE                                     # 回复最初状态.
 *   #!INCLUDE "/path/to/file"                  # 包含另一个属性文件.
 *                                              # 该文件的所有属性受上述BASE命令控制.
 *   # 更灵活的表示方法
 *   # 可以出现以下转义符: "\n", "\r", "\f", "\b", "\t", "\\", "\'", "\""等.
 *   double_quote_string = "he said, \"hello\"\n"
 *   simple_string = he said\, "hello"\n
 *   # 单引号字符串不转义:
 *   single_quote_string = 'this is NOT a carriage return \n'
 *   # 大块字符的表示
 *   chunk = &lt;&lt;END_OF_CHUNK                     # 以下是一块文字, 里面也可以包括转义字符.
 *     This
 *         is a
 *      fREe
 *        format
 *             DesCRiption.
 * END_OF_CHUNK                                 # 注意: 这一行一定要顶格
 *   # 无转义的大块字符的表示
 *   chunk = &lt;END_OF_CHUNK                      # 注意, 只有一个"&lt;".  这种方式不转义字符.
 *     This
 *         is a
 *      fREe
 *        format
 *             DesCRiption.
 * END_OF_CHUNK                                 # 注意: 这一行一定要顶格
 *   # 可以直接写明数组的下标, 数组可以是多维的.
 *   array[0] = 1
 *   array[1] = 2
 *   array[1][0] = 3
 *   array[1][1] = 4
 *   # 不规则的key, 可以用单引号或双引号括起来.
 *   calculate."1 + 2 + 3".value = 6
 *   # 可以引用别的属性.
 *   my.class.path = ${sys.class.path}:/a/b/c.jar               # ${...}将被取代.
 *   escape.the.reference = \${sys.class.path}:/a/b/c.jar       # 可以用\${...}避免引用.
 * </pre>
 * 本解析器没有被设计成线程安全的, 所以不能被多个线程同时访问.
 * </p>
 *
 * @version $Id: StandardParser.java 1291 2005-03-04 03:23:30Z baobao $
 * @author <a href="mailto:zyh@alibaba-inc.com">Michael Zhou</a>
 * @see java.util.Properties
 * @see com.alibaba.common.property.Property
 * @see com.alibaba.common.property.parser.Parser
 */
public class StandardParser implements Parser, StandardParserConstants {
    /** Parser记录器. */
    protected static final Logger log = LoggerFactory.getLogger("property.StandardParser");

    /** 默认encoding方法. */
    protected static final String DEFAULT_ENCODING = "UTF8";

    /** 处理parse过程中的事件. */
    private ParseHandler handler;

    /** 当前的encoding方法. */
    private String encoding = DEFAULT_ENCODING;

    /** 是否使用和Java一样的Unicode Escape方式. */
    private boolean supportUnicodeEscape = false;

    /** 取得当前的行号列号信息. */
    private final Locator locator = new Locator() {
        public int getLineNumber() {
            return (token == null)            ? 0
                                              : token.endLine;
        }

        public int getColumnNumber() {
            return (token == null)            ? 0
                                              : token.endColumn;
        }

        public String getResourceId() {
            return "not available";
        }

        public String toString() {
            return "line " + getLineNumber() + ", column " + getColumnNumber();
        }
    };

    /**
     * 默认构造方法, 构造一个空的parser. 使用前需要用init方法初始化.
     */
    public StandardParser() {
        this(new SimpleCharStreamCR(new StringReader("\n"), 1, 1));
    }

    /**
     * 返回当前的编码方式.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 如果当前解析器支持转义unicode字符, 则返回<code>true</code>.
     */
    public boolean unicodeEscapeSupported() {
        return supportUnicodeEscape;
    }

    /**
     * 设置Parse处理对象.
     */
    public void setParseHandler(ParseHandler handler) {
        this.handler = handler;
    }

    /**
     * 接收输入流.  输入流必须是支持<code>mark/reset</code>的.
     *
     * <p>
     * 该方法首先尝试分析输入流的第一行, 从而判别文件的格式.  如果文件首行包含了 属性文件的声明: <code>#!properties encoding="..."
     * uesc="..."</code>, 则正确的<code>encoding</code>和<code>unicode escape</code>方法将被设置.
     * 否则使用默认的<code>encoding</code>(<code>UTF8</code>), <code>unicode escape</code>
     * 为<code>no</code>.
     * </p>
     *
     * <p>
     * 输入流被接收以后, 就可以执行<code>parse()</code>方法了.
     * </p>
     *
     * @param is 输入流
     *
     * @return 如果文件被识别, 则返回<code>true</code>.  但返回<code>false</code>并不意味 着的文件格式非法,
     *         只是说明文件没有写<code>#!properties...</code>语句.
     *
     * @throws com.alibaba.common.property.parser.ParseException
     */
    public boolean accept(InputStream is)
            throws com.phoenix.common.property.parser.ParseException {
        try {
            Reader  reader    = null;
            byte[]  lookAhead = new byte[200];
            boolean accepted  = false;


            // 设置初始值.
            encoding             = DEFAULT_ENCODING;
            supportUnicodeEscape = false;


            // 预读输入流.
            is.mark(lookAhead.length);

            int bytesRead = is.read(lookAhead);

            is.reset();

            // 分析第一行.
            int len = 0;

            for (int i = 0; i < bytesRead; i++) {
                if (lookAhead[i] == '\r') {
                    if (((i + 1) < bytesRead) && (lookAhead[i + 1] == '\n')) {
                        len = i + 2;
                    } else {
                        len = i + 1;
                    }

                    break;
                } else if (lookAhead[i] == '\n') {
                    len = i + 1;
                    break;
                }
            }

            if (len > 0) {
                reader = new StringReader(new String(lookAhead, 0, len, encoding));
                ReInit(supportUnicodeEscape
                           ? (CharStream) new JavaCharStreamCR(reader, 1, 1)
                           : (CharStream) new SimpleCharStreamCR(reader, 1, 1));

                try {
                    // 试着分析第一行, 如果成功, 则encoding和supportUnicodeEscape将被设置成指定值.
                    token_source.clearStateStack();
                    DirectiveDeclare();


                    // 第一行分析成功, 重新初始化parser, 使之继续读取后续行.
                    accepted = true;
                    is.skip(len);
                    reader = new InputStreamReader(is, encoding);
                    ReInit(supportUnicodeEscape
                               ? (CharStream) new JavaCharStreamCR(reader, 2, 1)
                               : (CharStream) new SimpleCharStreamCR(reader, 2, 1));
                } catch (ParseException e) {
                    // 失败则恢复到默认值.
                    encoding             = DEFAULT_ENCODING;
                    supportUnicodeEscape = false;
                }
            }

            // 分析第一行失败, 使用默认值初始化parser.
            if (!accepted) {
                reader = new InputStreamReader(is, encoding);
                ReInit(supportUnicodeEscape
                           ? (CharStream) new JavaCharStreamCR(reader, 1, 1)
                           : (CharStream) new SimpleCharStreamCR(reader, 1, 1));
            }

            if (log.isDebugEnabled()) {
                log.debug("Parser initialized.");
                log.debug("Current encoding is set to " + encoding
                          + (supportUnicodeEscape ? ", and supports Java-style Unicode Escaping"
                                                  : "") + ".");
            }

            return accepted;
        } catch (IOException e) {
            throw new com.phoenix.common.property.parser.ParseException(e);
        }
    }

    /**
     * 开始解析文件.
     *
     * @throws com.phoenix.common.property.parser.ParseException
     */
    public void parse() throws com.phoenix.common.property.parser.ParseException {
        if (handler == null) {
            throw new com.phoenix.common.property.parser.ParseException(
                    "Parse handler should be set before parsing.");
        }

        token_source.clearStateStack();
        handler.setLocator(locator);
        handler.start();

        try {
            Start();
        } catch (ParseException e) {
            throw new com.phoenix.common.property.parser.ParseException(e);
        } catch (TokenMgrError tme) {
            throw new com.phoenix.common.property.parser.ParseException(tme);
        } catch (PassException pe) {
            throw pe.exception;
        }

        handler.end();
    }

    /**
     * 由于JavaCC生成的程序不能throw别的exception, 只好通过这个类, 把Handler里面的 exception"偷运"出来.
     */
    private static class PassException extends RuntimeException {
        private static final long serialVersionUID = 3257566200484344627L;
        com.phoenix.common.property.parser.ParseException exception;

        public PassException(com.phoenix.common.property.parser.ParseException e) {
            exception = e;
        }
    }

    /* ======================================================================
     * 定义非终结符。
     * ====================================================================== */

    /**
     * 解析property文件.
     */
    public final void Start() throws ParseException {
        label_1:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk()
                                   : jj_ntk) {
                case INTEGER_LITERAL:
                case IDENTIFIER:
                case LBRACKET:
                case VAR:
                case SQ_STRING:
                case DQ_STRING:
                case DIRECTIVE_DECLARE:
                case DIRECTIVE_INCLUDE:
                case DIRECTIVE_BASE:
                case DIRECTIVE_NAME:
                    ;
                    break;

                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }

            switch ((jj_ntk == -1) ? jj_ntk()
                                   : jj_ntk) {
                case DIRECTIVE_DECLARE:
                    DirectiveDeclare();
                    break;

                case DIRECTIVE_INCLUDE:
                    DirectiveInclude();
                    break;

                case DIRECTIVE_BASE:
                    DirectiveBase();
                    break;

                case DIRECTIVE_NAME:
                    DirectiveName();
                    break;

                case INTEGER_LITERAL:
                case IDENTIFIER:
                case LBRACKET:
                case VAR:
                case SQ_STRING:
                case DQ_STRING:
                    PropertyDefinition();
                    break;

                default:
                    jj_la1[1] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }

        jj_consume_token(0);
    }

    /**
     * 声明property文件(encoding等). 这是一个特殊的指令, 只能出现在文件的第一行第一列. 这条指令不会被handler接收到.
     */
    public final void DirectiveDeclare() throws ParseException {
        String s;

        jj_consume_token(DIRECTIVE_DECLARE);

        if ((token.beginLine != 1) || (token.beginColumn != 1)) {
            {
                if (true) {
                    throw new ParseException(
                            "The properties declaration may only appear at the very beginning of the document: "
                            + "Encountered \"" + TokenMgrError.addEscapes(token.image)
                            + "\" at line " + token.beginLine + ", column " + token.beginColumn
                            + ".");
                }
            }
        }

        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case ENCODING:
                jj_consume_token(ENCODING);
                jj_consume_token(EQUALS);
                s        = StringLiteral();
                encoding = s;
                break;

            default:
                jj_la1[2] = jj_gen;
                ;
        }

        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case UESC:
                jj_consume_token(UESC);
                supportUnicodeEscape = true;
                break;

            default:
                jj_la1[3] = jj_gen;
                ;
        }

        jj_consume_token(CR);
    }

    /**
     * 包含其它文件的指令.
     */
    public final void DirectiveInclude() throws ParseException {
        String resource;

        jj_consume_token(DIRECTIVE_INCLUDE);
        resource = StringLiteral();

        try {
            handler.include(resource);
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        jj_consume_token(CR);
    }

    /**
     * 指明property的基.
     */
    public final void DirectiveBase() throws ParseException {
        jj_consume_token(DIRECTIVE_BASE);

        try {
            handler.startBase();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case INTEGER_LITERAL:
            case IDENTIFIER:
            case LBRACKET:
            case VAR:
            case SQ_STRING:
            case DQ_STRING:
                Property();
                break;

            default:
                jj_la1[4] = jj_gen;
                ;
        }

        try {
            handler.endBase();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        jj_consume_token(CR);
    }

    /**
     * 指明property文件的名称, 也就是根属性的键值.
     */
    public final void DirectiveName() throws ParseException {
        String name;

        jj_consume_token(DIRECTIVE_NAME);
        name = StringLiteral();

        try {
            handler.setName(name);
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        jj_consume_token(CR);
    }

    /**
     * 解析一条property的定义.
     */
    public final void PropertyDefinition() throws ParseException {
        try {
            handler.startPropertyDefinition();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        Property();
        jj_consume_token(EQUALS);
        Value();
        label_2:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk()
                                   : jj_ntk) {
                case COMMA:
                    ;
                    break;

                default:
                    jj_la1[5] = jj_gen;
                    break label_2;
            }

            jj_consume_token(COMMA);
            Value();
        }

        try {
            handler.endPropertyDefinition();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        jj_consume_token(CR);
    }

    /**
     * 解析一个property.
     */
    public final void Property() throws ParseException {
        String key;
        int    index = -1;

        try {
            handler.startProperty();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }

        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case VAR:
                jj_consume_token(VAR);

                try {
                    handler.propertyVar(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case INTEGER_LITERAL:
            case IDENTIFIER:
            case SQ_STRING:
            case DQ_STRING:
                key = StringLiteral();

                try {
                    handler.propertyKey(key);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case LBRACKET:
                jj_consume_token(LBRACKET);

                switch ((jj_ntk == -1) ? jj_ntk()
                                       : jj_ntk) {
                    case INTEGER_LITERAL:
                        index = IntegerLiteral();
                        break;

                    default:
                        jj_la1[6] = jj_gen;
                        ;
                }

                jj_consume_token(RBRACKET);

                try {
                    handler.propertyIndex(index);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                index = -1;
                break;

            default:
                jj_la1[7] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }

        label_3:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk()
                                   : jj_ntk) {
                case DOT:
                case LBRACKET:
                    ;
                    break;

                default:
                    jj_la1[8] = jj_gen;
                    break label_3;
            }

            switch ((jj_ntk == -1) ? jj_ntk()
                                   : jj_ntk) {
                case DOT:
                    jj_consume_token(DOT);

                    switch ((jj_ntk == -1) ? jj_ntk()
                                           : jj_ntk) {
                        case VAR:
                            jj_consume_token(VAR);

                            try {
                                handler.propertyVar(token.image);
                            } catch (com.phoenix.common.property.parser.ParseException e) {
                                {
                                    if (true) {
                                        throw new PassException(e);
                                    }
                                }
                            }

                            break;

                        case INTEGER_LITERAL:
                        case IDENTIFIER:
                        case SQ_STRING:
                        case DQ_STRING:
                            key = StringLiteral();

                            try {
                                handler.propertyKey(key);
                            } catch (com.phoenix.common.property.parser.ParseException e) {
                                {
                                    if (true) {
                                        throw new PassException(e);
                                    }
                                }
                            }

                            break;

                        default:
                            jj_la1[9] = jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }

                    break;

                case LBRACKET:
                    jj_consume_token(LBRACKET);

                    switch ((jj_ntk == -1) ? jj_ntk()
                                           : jj_ntk) {
                        case INTEGER_LITERAL:
                            index = IntegerLiteral();
                            break;

                        default:
                            jj_la1[10] = jj_gen;
                            ;
                    }

                    jj_consume_token(RBRACKET);

                    try {
                        handler.propertyIndex(index);
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }

                    index = -1;
                    break;

                default:
                    jj_la1[11] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }

        try {
            handler.endProperty();
        } catch (com.phoenix.common.property.parser.ParseException e) {
            {
                if (true) {
                    throw new PassException(e);
                }
            }
        }
    }

    /**
     * Property的单个值.
     */
    public final void Value() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case SQ_STRING:
                jj_consume_token(SQ_STRING);

                try {
                    handler.value(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case SQ_BLOCK:
                jj_consume_token(SQ_BLOCK);

                try {
                    handler.value(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case DQ_STRING:
            case DQ_STRING_PART:
                label_4:
                while (true) {
                    switch ((jj_ntk == -1) ? jj_ntk()
                                           : jj_ntk) {
                        case DQ_STRING_PART:
                            ;
                            break;

                        default:
                            jj_la1[12] = jj_gen;
                            break label_4;
                    }

                    jj_consume_token(DQ_STRING_PART);

                    try {
                        handler.startReference(token.image);
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }

                    Property();

                    try {
                        handler.endReference();
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }
                }

                jj_consume_token(DQ_STRING);

                try {
                    handler.value(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case DQ_BLOCK_PART:
            case DQ_BLOCK:
                label_5:
                while (true) {
                    switch ((jj_ntk == -1) ? jj_ntk()
                                           : jj_ntk) {
                        case DQ_BLOCK_PART:
                            ;
                            break;

                        default:
                            jj_la1[13] = jj_gen;
                            break label_5;
                    }

                    jj_consume_token(DQ_BLOCK_PART);

                    try {
                        handler.startReference(token.image);
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }

                    Property();

                    try {
                        handler.endReference();
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }
                }

                jj_consume_token(DQ_BLOCK);

                try {
                    handler.value(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            case SIMPLE_STRING_PART:
            case SIMPLE_STRING:
                label_6:
                while (true) {
                    switch ((jj_ntk == -1) ? jj_ntk()
                                           : jj_ntk) {
                        case SIMPLE_STRING_PART:
                            ;
                            break;

                        default:
                            jj_la1[14] = jj_gen;
                            break label_6;
                    }

                    jj_consume_token(SIMPLE_STRING_PART);

                    try {
                        handler.startReference(token.image);
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }

                    Property();

                    try {
                        handler.endReference();
                    } catch (com.phoenix.common.property.parser.ParseException e) {
                        {
                            if (true) {
                                throw new PassException(e);
                            }
                        }
                    }
                }

                jj_consume_token(SIMPLE_STRING);

                try {
                    handler.value(token.image);
                } catch (com.phoenix.common.property.parser.ParseException e) {
                    {
                        if (true) {
                            throw new PassException(e);
                        }
                    }
                }

                break;

            default:
                jj_la1[15] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    /**
     * 取得一个无换行的字符串.
     */
    public final String StringLiteral() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk()
                               : jj_ntk) {
            case INTEGER_LITERAL:
                jj_consume_token(INTEGER_LITERAL);
                break;

            case IDENTIFIER:
                jj_consume_token(IDENTIFIER);
                break;

            case SQ_STRING:
                jj_consume_token(SQ_STRING);
                break;

            case DQ_STRING:
                jj_consume_token(DQ_STRING);
                break;

            default:
                jj_la1[16] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }

        {
            if (true) {
                return token.image;
            }
        }

        throw new Error("Missing return statement in function");
    }

    /**
     * 取得一个整数.
     */
    public final int IntegerLiteral() throws ParseException {
        jj_consume_token(INTEGER_LITERAL);

        {
            if (true) {
                return Integer.decode(token.image).intValue();
            }
        }

        throw new Error("Missing return statement in function");
    }

    public StandardParserTokenManager token_source;
    public Token                      token;
    public Token                      jj_nt;
    private int                       jj_ntk;
    private int                       jj_gen;
    private final int[]               jj_la1   = new int[17];
    private final int[]               jj_la1_0 = {
        0x2b0000,
        0x2b0000,
        0x4000,
        0x8000,
        0x2b0000,
        0x800000,
        0x10000,
        0x2b0000,
        0xc0000,
        0x230000,
        0x10000,
        0xc0000,
        0x0,
        0x0,
        0x0,
        0x0,
        0x30000,
    };
    private final int[]               jj_la1_1 = {
        0x1e88,
        0x1e88,
        0x0,
        0x0,
        0x88,
        0x0,
        0x0,
        0x88,
        0x0,
        0x88,
        0x0,
        0x0,
        0x80000,
        0x0,
        0x0,
        0x2080088,
        0x88,
    };
    private final int[]               jj_la1_2 = {
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x0,
        0x2,
        0x1000,
        0x3006,
        0x0,
    };

    public StandardParser(CharStream stream) {
        token_source = new StandardParserTokenManager(stream);
        token        = new Token();
        jj_ntk       = -1;
        jj_gen       = 0;

        for (int i = 0; i < 17; i++) {
            jj_la1[i] = -1;
        }
    }

    public void ReInit(CharStream stream) {
        token_source.ReInit(stream);
        token  = new Token();
        jj_ntk = -1;
        jj_gen = 0;

        for (int i = 0; i < 17; i++) {
            jj_la1[i] = -1;
        }
    }

    public StandardParser(StandardParserTokenManager tm) {
        token_source = tm;
        token        = new Token();
        jj_ntk       = -1;
        jj_gen       = 0;

        for (int i = 0; i < 17; i++) {
            jj_la1[i] = -1;
        }
    }

    public void ReInit(StandardParserTokenManager tm) {
        token_source = tm;
        token        = new Token();
        jj_ntk       = -1;
        jj_gen       = 0;

        for (int i = 0; i < 17; i++) {
            jj_la1[i] = -1;
        }
    }

    private final Token jj_consume_token(int kind)
            throws ParseException {
        Token oldToken;

        if ((oldToken = token).next != null) {
            token = token.next;
        } else {
            token = token.next = token_source.getNextToken();
        }

        jj_ntk = -1;

        if (token.kind == kind) {
            jj_gen++;
            return token;
        }

        token   = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    public final Token getNextToken() {
        if (token.next != null) {
            token = token.next;
        } else {
            token = token.next = token_source.getNextToken();
        }

        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index) {
        Token t = token;

        for (int i = 0; i < index; i++) {
            if (t.next != null) {
                t = t.next;
            } else {
                t = t.next = token_source.getNextToken();
            }
        }

        return t;
    }

    private final int jj_ntk() {
        if ((jj_nt = token.next) == null) {
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        } else {
            return (jj_ntk = jj_nt.kind);
        }
    }

    @SuppressWarnings("unchecked")
	private java.util.Vector jj_expentries = new java.util.Vector();
    private int[]            jj_expentry;
    private int              jj_kind = -1;

    @SuppressWarnings("unchecked")
	public final ParseException generateParseException() {
        jj_expentries.removeAllElements();

        boolean[] la1tokens = new boolean[81];

        for (int i = 0; i < 81; i++) {
            la1tokens[i] = false;
        }

        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind            = -1;
        }

        for (int i = 0; i < 17; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }

                    if ((jj_la1_1[i] & (1 << j)) != 0) {
                        la1tokens[32 + j] = true;
                    }

                    if ((jj_la1_2[i] & (1 << j)) != 0) {
                        la1tokens[64 + j] = true;
                    }
                }
            }
        }

        for (int i = 0; i < 81; i++) {
            if (la1tokens[i]) {
                jj_expentry    = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }

        int[][] exptokseq = new int[jj_expentries.size()][];

        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = (int[]) jj_expentries.elementAt(i);
        }

        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }
}
