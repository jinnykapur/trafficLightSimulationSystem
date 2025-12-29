# 🚦 Traffic Light Control System – Java Swing Simulation

A desktop-based **Traffic Signal Control System Simulation** developed using **Java and Swing**.
The system simulates real-world traffic signals with **traffic density control** and an **emergency mode**.

---

## 📌 Project Features

* Real-time simulation of **Red, Yellow, and Green** traffic lights
* **Traffic Density–based timing** (Low, Medium, High)
* **Emergency Mode** with blinking yellow signal
* Live **countdown timer display**
* Interactive **control panel**
* Multithreaded animation for smooth execution

---

## 🛠 Tools & Technologies

* **Programming Language:** Java
* **Framework:** Java Swing
* **Libraries:** Java AWT, Multithreading
* **IDE:** Visual Studio Code
* **Platform:** Windows / Linux / macOS

---

## 🧩 Project Structure

| File / Class                  | Description                            |
| ----------------------------- | -------------------------------------- |
| `TrafficLightSimulation.java` | Main controller class                  |
| `TrafficLightModel.java`      | Handles signal logic & traffic density |
| `TrafficLight.java`           | Represents individual traffic lights   |
| `Animation.java`              | Controls signal transitions & timing   |
| `DrawingPanel.java`           | Draws traffic light UI                 |
| `ControlPanel.java`           | User interaction controls              |
| `RoundedButton.java`          | Custom button UI component             |

---

## ▶ How to Run

1. Install **JDK 8 or above**
2. Open the project folder in **Visual Studio Code**
3. Open terminal and compile:

   ```bash
   javac TrafficLightSimulation.java
   ```
4. Run the program:

   ```bash
   java TrafficLightSimulation
   ```
5. Use **Start / Stop / Emergency** buttons to control simulation.

---

## 🚨 Emergency Mode

* Activates blinking **Yellow Signal**
* Simulates emergency situations like ambulance movement.

---

## 📊 Traffic Density Control

| Density | Green Light Duration |
| ------- | -------------------- |
| Low     | Short                |
| Medium  | Moderate             |
| High    | Long                 |

---
## 📷 Screenshots (in Appendix)

* Code Snippet
  <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/8594ac39-5ae5-4f27-8bfd-31c62e46f40a" />
* User Interface
  <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/717c62bd-8ebc-4fca-b47d-43d9c0ecbe57" />
* UML Diagram
  <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/9f86c900-ca94-4722-995d-0cf9849d6c5e" />
* Emergency Mode
  <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/759f0ed7-71be-4d4a-9dcf-8da1505729dd" />
* Traffic Density Mode
  <img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/f2d0a6bc-4f29-45c3-9524-59ee209d4ae9" />
---

## 🏁 Conclusion

This project demonstrates an interactive and adaptive traffic signal simulation system using Java Swing, making it ideal for educational and demonstration purposes.

---
