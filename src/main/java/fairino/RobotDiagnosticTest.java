package fairino;

public class RobotDiagnosticTest {
    public static void main(String[] args) {
        Robot robot = new Robot();

        try {
            // Watchdog and connection parameters
            robot.SetReconnectParam(true, 20, 500);
            robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "C://RobotLogs/", 10, 10);

            // Target workstation simulation IP
            String controllerIp = "192.168.222.129";
            int connectionResult = robot.RPC(controllerIp);

            if (connectionResult != 0) {
                System.err.println("[FATAL] Link with controller failed. Code: " + connectionResult);
                return;
            }
            System.out.println("[SYSTEM] Active communication channel online.");

            // Set manual mode (1) to permit physical jog movements safely
            robot.Mode(1);
            robot.Sleep(500);

            // Execute the joint and coordinate axis diagnostic sequence
            int testResult = runAxisDiagnosticSequence(robot);
            System.out.println("[INFO] Diagnostic sequence completed with return code: " + testResult);

        } catch (Exception ex) {
            System.err.println("[CRITICAL] Diagnostic sequence exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Hardware teardown sequence
            robot.CloseRPC();
            System.out.println("[SYSTEM] Network link closed cleanly.");
        }
    }

    public static int runAxisDiagnosticSequence(Robot robot) {
        // Sequence 1: Joint Jogging (RefType 0, StopType 1)
        System.out.println("[TEST] Starting Joint space jog diagnostics...");
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(0, i + 1, 1, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(1);
            robot.Sleep(1000);
        }

        // Sequence 2: Base Coordinate Jogging (RefType 2, StopType 3)
        System.out.println("[TEST] Starting Base coordinate space jog diagnostics...");
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(2, i + 1, 1, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(3);
            robot.Sleep(1000);
        }

        // Sequence 3: Tool Coordinate Jogging (RefType 4, StopType 5)
        System.out.println("[TEST] Starting Tool coordinate space jog diagnostics...");
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(4, i + 1, 1, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(5);
            robot.Sleep(1000);
        }

        // Sequence 4: Workpiece Coordinate Jogging (RefType 8, StopType 9)
        System.out.println("[TEST] Starting Workpiece coordinate space jog diagnostics...");
        for (int i = 0; i < 6; i++) {
            robot.StartJOG(8, i + 1, 1, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(9);
            robot.Sleep(1000);
        }

        return 0;
    }
}