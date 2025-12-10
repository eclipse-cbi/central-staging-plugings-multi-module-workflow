package org.eclipse.cbi.central.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreServiceTest {
    
    @Test
    void testProcess() {
        CoreService service = new CoreService();
        assertEquals("Processed: hello", service.process("hello"));
    }
    
    @Test
    void testProcessEmpty() {
        CoreService service = new CoreService();
        assertEquals("No message provided", service.process(""));
    }
    
    @Test
    void testCalculate() {
        CoreService service = new CoreService();
        assertEquals(10, service.calculate(5));
    }
}
