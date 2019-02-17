package cn.webank.blockchain.spi.common.exception;

public class IndexFieldParsingFailedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IndexFieldParsingFailedException() {
        super();
    }

    public IndexFieldParsingFailedException(String message, Throwable cause, boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IndexFieldParsingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexFieldParsingFailedException(String message) {
        super(message);
    }

    public IndexFieldParsingFailedException(Throwable cause) {
        super(cause);
    }


}
