# Fairino Java SDK Examples

This repository contains practical examples and standalone configurations for controlling the Fairino robot manipulator using the native **Fairino Java SDK**. 

 • SDK Version: v3.9.6
 
 • Fairino WebApp version: 3.9.6

---

## Hardware & Environment Support

* **Robot Deployment:** Compatible with the physical **Fairino 5 V6** industrial manipulator controller.
* **Testing Environment:** Validated locally using VMware Workstation (Ubuntu 22.04 LTS Controller Simulation).
* **IDE:** IntelliJ IDEA
* **Build Tool:** Apache Maven
* **SDK Version:** Fairino Java SDK v3.9.6

**Network Interface:** The robot controller (whether using the physical arm network or a local simulation instance) must be active and accessible over the local network via its static IP address (e.g., `192.168.222.129`).

---

## Repository Structure

* **lib/** → Native SDK dependencies and custom communication libraries  
* **pom.xml** → Maven configuration, dependencies tracking, and build lifecycle metrics  
* **src/main/java/fairino/** → Automated motion execution and test profiles:
  * `PickAndPlaceAutomation.java` → Cartesian pick‑and‑place routine (`fairino.PickAndPlaceAutomation`)
  * `RobotDiagnosticTest.java` → Low‑level multiaxial JOG diagnostics (`fairino.RobotDiagnosticTest`)
  * `controlJog.java` → Official OEM manual JOG specification routine (`fairino.controlJog`)

---

## Installation & Environment Setup

### 1. Project Initialization
Clone this repository or extract the project environment structure into your local development workspace directory:
cd ~/fairino-java-project/
git clone [https://github.com/inlux-robotics/java-sdk.git](https://github.com/inlux-robotics/java-sdk.git)

2. Open Project in IntelliJ IDEA
Launch IntelliJ IDEA and select Open.

Navigate to the root directory containing the file setup profile:

~/fairino-java-project/fairino-java-sdk-main/fairino_Java_SDK-maven/

Click OK to load the workspace.

3. Resolve Dependencies via Maven
IntelliJ IDEA will parse the pom.xml configuration automatically. When prompted by the system layout, click "Load Maven Changes" to resolve the core network dependencies:

XML
<dependencies>
    <dependency>
        <groupId>org.apache.xmlrpc</groupId>
        <artifactId>xmlrpc-client</artifactId>
        <version>3.1.3</version>
    </dependency>
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.1</version>
    </dependency>
</dependencies>

Running the Examples

1. Pick and Place Automation (PickAndPlaceAutomation.java)

   
Runs a continuous, high-speed automated industrial pick-and-place routine. It targets automatic execution mode (Mode(2)), sets speed thresholds to 70%, and uses strict linear interpolation (MoveL) for vertical clearance along with workspace corner blending (blendRadiusmm = 30.0).

Execution: Open the file in IntelliJ IDEA and click the green Play (▶) button next to the main method declaration.

[![Watch Pick and Place Video](https://img.youtube.com/vi/eN0LwGBVbWg/0.jpg)](https://youtu.be/eN0LwGBVbWg)



[Watch the video](https://youtu.be/eN0LwGBVbWg)


2. Multiaxial JOG Diagnostics (RobotDiagnosticTest.java)

   
Executes safe axis rotation and frame verification sequences. It steps through Joint Space, Base System Coordinates, Tool System Coordinates, and Custom Workpiece Frames, deploying controlled directional jogs before clearing the motion registers with explicit halt flags.

Execution: Right-click the class file in the IntelliJ project layout hierarchy and select Run 'RobotDiagnosticTest.main()'.

[![Watch Multiaxial JOG Diagnostics Video](https://img.youtube.com/vi/LZYaZRTk14w/0.jpg)](https://youtu.be/LZYaZRTk14w)


[Watch the video](https://youtu.be/LZYaZRTk14w)


3. Official OEM Manual JOG Specification (controlJog.java)

   
Executes the standardized manual JOG benchmark sequence directly derived from Section 4.4 of the official manufacturer specifications. It systematically steps through Joint Space, Base System, Tool System, and Workpiece Frame offsets using targeted one-second motion pulses before executing immediate and controlled safety stops.

Execution: Open the file in IntelliJ IDEA and click the green Play (▶) button next to the main method declaration.

[![Watch OEM JOG Manual Specification Video](https://img.youtube.com/vi/u8rZudD9-gY/0.jpg)](https://youtu.be/u8rZudD9-gY)



[Watch the video](https://youtu.be/u8rZudD9-gY)


Contact & Support
Email: support@inluxrobotics.com
