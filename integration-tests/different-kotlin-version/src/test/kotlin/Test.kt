import org.junit.Test

import jb.PrivateFields.priv
import org.junit.Assert

class Test {
    
    @Test
    public fun test() {
        val o = PrivateFields()
        Assert.assertEquals("123", o.priv())
    }
    
}