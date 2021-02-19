package app;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class AprioriController implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        return null;
    }
}
