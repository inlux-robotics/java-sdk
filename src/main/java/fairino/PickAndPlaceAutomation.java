package fairino;

import java.util.ArrayList;
import java.util.List;

public class PickAndPlaceAutomation {
        public static void main(String[] args) {
                Robot robot = new Robot();

                try {
                        // Connection watchdog settings
                        robot.SetReconnectParam(true, 20, 500);
                        robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "C://RobotLogs/", 10, 10);

                        // Controller IP allocation (VMware local network)
                        String controllerIp = "192.168.222.129";
                        int connectionResult = robot.RPC(controllerIp);

                        if (connectionResult != 0) {
                                System.err.println("[FATAL] Failed to link with robot controller. Code: " + connectionResult);
                                return;
                        }
                        System.out.println("[SYSTEM] Active connection verified with controller.");

                        // Set automatic mode for fluid kinematic performance
                        robot.Mode(2);
                        robot.Sleep(200);

                        // Power up joint actuators
                        robot.RobotEnable(1);
                        robot.Sleep(500);

                        // ====================================================================
                        // VELOCITY & SYSTEM CONFIGURATIONS
                        // ====================================================================
                        int toolIndex = 1;
                        int userIndex = 1;

                        double speedPercent = 70.0;
                        double accelPercent = 80.0;
                        double speedScaling = 100.0;

                        // Blend path calculation to eliminate clunky joint decelerations
                        double blendRadiusmm = 30.0;

                        ExaxisPos externalAxes = new ExaxisPos(0, 0, 0, 0);
                        DescPose zeroOffset = new DescPose(0, 0, 0, 0, 0, 0);

                        // ====================================================================
                        // TARGET WORKING WAYPOINTS
                        // ====================================================================
                        // Orientation optimized with rotated Z-axis to keep arm joints forward
                        DescPose safeHome = new DescPose(300.0, 0.0, 350.0, 180.0, 0.0, 180.0);

                        // Pickup coordinates
                        DescPose pickClearance = new DescPose(350.0, -150.0, 200.0, 180.0, 0.0, 180.0);
                        DescPose pickTarget    = new DescPose(350.0, -150.0, 100.0, 180.0, 0.0, 180.0);

                        // Dropoff coordinates
                        DescPose placeClearance = new DescPose(350.0, 150.0, 200.0, 180.0, 0.0, 180.0);
                        DescPose placeTarget    = new DescPose(350.0, 150.0, 100.0, 180.0, 0.0, 180.0);

                        // ====================================================================
                        // WORKCELL PATH EXECUTION
                        // ====================================================================

                        // Move 1: PTP Joint trajectory to home to unwind the arm structure safely
                        System.out.println("[MOVE] Unfolding mechanism to baseline posture...");
                        robot.MoveCart(safeHome, toolIndex, userIndex, speedPercent, accelPercent, speedScaling, -1.0, -1);

                        // Move 2: Transit to pickup staging plane with corner blending enabled
                        System.out.println("[EXEC] Approaching pickup station...");
                        robot.MoveL(pickClearance, toolIndex, userIndex, speedPercent, accelPercent, speedScaling,
                                blendRadiusmm, 0, externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // Move 3: Strict linear vertical approach to part interface plane
                        System.out.println("[EXEC] Vertical descent to component nest...");
                        robot.MoveL(pickTarget, toolIndex, userIndex, speedPercent, accelPercent, speedScaling, -1.0, 0,
                                externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // End-effector engagement interval
                        System.out.println("[IO] Engaging actuator tool circuit...");
                        robot.Sleep(500);

                        // Move 4: Pure linear vertical lift with part secured
                        System.out.println("[EXEC] Vertical clearance extraction...");
                        robot.MoveL(pickClearance, toolIndex, userIndex, speedPercent, accelPercent, speedScaling,
                                blendRadiusmm, 0, externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // Move 5: Linear trajectory across tracking boundaries to target cell zone
                        System.out.println("[EXEC] Cross-travel transit to discharge clearance point...");
                        robot.MoveL(placeClearance, toolIndex, userIndex, speedPercent, accelPercent, speedScaling,
                                blendRadiusmm, 0, externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // Move 6: Controlled linear tool approach onto target structure interface
                        System.out.println("[EXEC] Final component placement descent...");
                        robot.MoveL(placeTarget, toolIndex, userIndex, speedPercent, accelPercent, speedScaling, -1.0,
                                0, externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // End-effector disengagement interval
                        System.out.println("[IO] Disengaging actuator tool circuit...");
                        robot.Sleep(500);

                        // Move 7: Vertical escape track to clear immediate fixtures
                        System.out.println("[EXEC] Vertical retreat clear of localized structures...");
                        robot.MoveL(placeClearance, toolIndex, userIndex, speedPercent, accelPercent, speedScaling,
                                blendRadiusmm, 0, externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        // Move 8: Linear return to home station to secure loop reset
                        System.out.println("[EXEC] Sequence complete. Returning to baseline posture.");
                        robot.MoveL(safeHome, toolIndex, userIndex, speedPercent, accelPercent, speedScaling, -1.0, 0,
                                externalAxes, 0, 0, zeroOffset, -1, 0, 0, 10);

                        robot.StopMotion();

                } catch (Exception ex) {
                        System.err.println("[CRITICAL] Operational thread exception: " + ex.getMessage());
                        ex.printStackTrace();
                } finally {
                        // Safe state teardown sequence
                        robot.RobotEnable(0);
                        robot.CloseRPC();
                        System.out.println("[SYSTEM] Robot network link closed safely.");
                }
        }
}