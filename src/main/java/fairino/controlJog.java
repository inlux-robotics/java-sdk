package fairino;

public class controlJog {
    public static void main(String[] args) {
        Robot robot = new Robot();
        
        try {
            // Comm watchdog config
            robot.SetReconnectParam(true, 20, 500);
            robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "C://RobotLogs/", 10, 10);

            // Connect to controller/simulation server
            String controllerIp = "192.168.222.129";
            int status = robot.RPC(controllerIp);

            if (status != 0) {
                System.err.println("[ERROR] Controller RPC connection failed. Code: " + status);
                return;
            }
            System.out.println("[SYS] Connected to robot controller.");

            // Switch to manual mode for JOG operations
            robot.Mode(1);
            robot.Sleep(500);

            // Execute OEM manual jog sequence
            int res = TestJOG(robot);
            System.out.println("[SYS] JOG test finished. Return code: " + res);

        } catch (Exception e) {
            System.err.println("[EXC] Runtime error during jog execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Safety halt and connection teardown
            robot.ImmStopJOG(); 
            robot.CloseRPC();
            System.out.println("[SYS] Connection closed cleanly.");
        }
    }

    // --- Official OEM Manual Test Spec (Section 4.4) ---
    public static int TestJOG(Robot robot) {
        // 1. Joint Space Jog
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(0, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.ImmStopJOG();
            robot.Sleep(1000);
        }
        
        // 2. Base Coordinate Jog
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(2, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.ImmStopJOG();
            robot.Sleep(1000);
        }
        
        // 3. Tool Coordinate Jog
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(4, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(5);
            robot.Sleep(1000);
        }
        
        // 4. Workpiece Coordinate Jog
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(8, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(9);
            robot.Sleep(1000);
        }
        
        return 0;
    }
}
