# Routing-Simulator

A small teaching and demonstration routing simulator written in Java (Spring Boot project layout).

This repository contains a lightweight routing simulator with modules for devices, routers,
DHCP, DNS server, and a few simple command utilities (ping, nslookup, nmap-like scanner).

**Project goals:**
- Provide a simple simulation environment for networking concepts (IP addressing, DHCP, DNS, routing)
- Offer small command modules to exercise the simulated network
- Serve as a learning codebase for students and contributors

**Status:** Use the instructions below to build, run, and extend.

**Table of Contents**
- **Project Overview**
- **Architecture & Important Classes**
- **Build & Run**
- **Testing**
- **Running the included Nmap-style Command**
- **Data files**
- **Contributors**
- **Development Notes & Guidelines**
- **Troubleshooting**
- **License**

---

**Project Overview**

The simulator models a simple network with managers (tenants), each managing devices and small services.
It includes the following subsystems:
- DHCP: generate and hand out private IPv4s
- DNS: small in-memory DNS backed by `dns_records.txt`
- Devices: simple models that can expose ports
- Commands: small CLI-like utilities implemented as Java classes (e.g. `NmapCommand`)

This code is arranged as a Spring Boot project structure but can be run from a plain JVM as needed.

**Architecture & Important Classes**
- **`GlobeManager`** (`modules/manager/GlobeManager.java`): top-level manager of the simulated world; holds `Manager` instances, the network graph, and a `DNSServer` instance.
- **`Manager`** (`modules/manager/Manager.java`): groups `Model` instances (devices, routers, DHCP, public servers) and assigns public IPs.
- **`Device`** (`modules/models/device/Device.java`): represents a host with an IPv4 and a list of active ports.
- **`DNSServer`** (`modules/models/DnsServer/DNSServer.java`): small DNS implementation backed by a `dns_records.txt` file.
- **`NmapCommand`** (`modules/commands/NmapCommand.java`): a simple TCP connection based port scanner that can operate against actual IPs or simulated `Device` objects managed by `GlobeManager`.

See the `routingSimulator/src/main/java/com/example/routingSimulator` tree for other modules (`network`, `models`, `service`, `Controller`).

**Build & Run**

Prerequisites:
- Java 11+ (JDK)
- Maven (`mvn`) on PATH

Build the project:
```bash
cd routingSimulator
mvn -DskipTests package
```

Run the Spring Boot application (if desired):
```bash
cd routingSimulator
mvn spring-boot:run
```

Or run a single class from `target/classes` using `java -cp` if you add a small main runner.

**Running the included Nmap-style Command (example)**

`NmapCommand` can be used in two ways:
- In-simulation: when the `GlobeManager` knows about a `Device` with the target IP, `NmapCommand` will report that device's `activePorts`.
- Live-scan mode: when not in `simulateOnly` mode the command will attempt real TCP connections to the provided IP/hostname.

Example usage from a small main method (create this in `routingSimulator/src/main/java/...`):

- Create a `GlobeManager` instance
- Retrieve the `DNSServer` via `globeManager.getDnsServer()` (it is pre-created by `GlobeManager`)
- Create a `Device`, add ports via `device.addPort(80)`, `device.addPort(22)`; add device to a `Manager` and register `Manager` in `GlobeManager`.
- Instantiate `NmapCommand` with `new NmapCommand(globeManager, globeManager.getDnsServer())` and call `scan(host, start, end, timeout)`.

**Data files**
- `dns_records.txt` (repo root and `routingSimulator/` copy): stores domain,ip pairs as `domain,ip` per line used by `DNSServer`.
- `data/services.txt`: optional service-to-port mapping used by `NmapCommand`.

**API Endpoints**

The application exposes a small set of REST endpoints via the controller layer. All endpoints return plain text responses from the corresponding service layer.

- **GET /**
	- Controller: `HomeController#index`
	- Description: Root index / welcome message for the application.

- **GET /play**
	- Controller: `PlayController#index`
	- Description: Entry point for the "play" section; returns a short welcome message.

- **Sandbox endpoints** (base: `/play/sandbox` — implemented in `SandboxController`)
	- **GET /play/sandbox** — `SandboxController#index`
		- Returns a welcome message for sandbox operations.
	- **POST /play/sandbox** — `SandboxController#createSandbox`
		- Parameters: `json` (form/query param) — JSON string describing a sandbox to create.
		- Creates a new sandbox and returns the created sandbox id or status.
	- **GET /play/sandbox/{id}** — `SandboxController#openSandbox`
		- Path variable: `id` (int) — open and view sandbox state.
	- **POST /play/sandbox/{id}** — `SandboxController#updateSandbox`
		- Path variable: `id` (int)
		- Parameters: `json` (form/query param) — JSON string with updates to apply to the sandbox.
	- **GET /play/sandbox/{id}/managers** — `SandboxController#getAllManagers`
		- Returns managers registered in the specified sandbox.
	- **GET /play/sandbox/{id}/{mid}/devices** — `SandboxController#getAllDevices`
		- Path variables: `id` (sandbox id), `mid` (manager id as string)
		- Returns devices belonging to the specified manager.
	- **GET /play/sandbox/{id}/network** — `SandboxController#getNetworkGraph`
		- Returns a graph visualization (SVG/string) for the sandbox network.

- **Command endpoints** (base: `/play/sandbox/{id}/command` — implemented in `CommandController`)
	- **GET /play/sandbox/{id}/command** — `CommandController#index`
		- Returns a small welcome message describing command usage for the sandbox.
	- **POST /play/sandbox/{id}/command** — `CommandController#updateCommand`
		- Path variable: `id` (int)
		- Parameters: `json` (form/query param) — JSON string describing a command to run (e.g., nmap/ping/nslookup invocation).
		- The `CommandService` will parse and execute the requested command against the sandbox and return textual output.

Notes:
- All `@RequestParam("json")` parameters are read as simple form/query parameters (not as a JSON request body). When calling via `curl` you can use `-d json='{"..."}'`.
- Endpoints currently return plain text (String). If you need JSON responses, consider updating the service/controller to return structured objects and annotate with `@ResponseBody`/`@RestController` behavior (already present) and appropriate content type handling.

# 📘 Project Contributors & API Documentation

## 👥 Contributors

All listed contributors made **equal and significant contributions** to this project. Their work involved **design, implementation, and testing** across the core modules — including **Manager**, **Models**, **Network Graph**, **Commands**, and **Services**.

| Contributor | Contributions |
|--------------|----------------|
| **Jos Samuel Biju** | Implemented the **N-map command**, developed `Device.java`, and handled **debugging and testing**. |
| **Macwan Darell** | Designed the **Abstract Model Class** and developed models such as **Router**, **Public Server**, and **DHCP**. Implemented **IP logic**. |
| **Vedansh Patel** | Developed **connection link functionality** and integrated **command operations**. |
| **Vedant Savani** | Designed and implemented the **Network Topology** and integrated **Dijkstra’s Algorithm** for pathfinding. |
| **Karan Mansuria** | Implemented the **DNS Server**, **NS-Lookup command**, and **file management** modules. |
| **Tanuj Shah** | Implemented the **Ping command** and the **Manager module**, and contributed to **debugging and testing**. |

---

## 🌐 API Endpoints

Below is the list of implemented API endpoints, along with their primary contributors and brief descriptions.

### **🔹 Macwan Darell**
```java
@GetMapping("/{id}/managers")
```
- Retrieves all managers within a specific sandbox.

```java
@GetMapping("/{id}/{mid}/devices")
```
- Lists all devices managed by a specific manager.

---

### **🔹 Jos Samuel Biju**
```java
@GetMapping("/{id}/{deviceid}")
```
- Lists all active ports for a specific device.

```java
@GetMapping("/{id}/network")
```
- Returns the complete network graph.

---

### **🔹 Vedansh Patel**
```java
@GetMapping("/{id}/links/{u}/{v}")
```
- Provides detailed information about the link between nodes *u* and *v*.

```java
@GetMapping("/{id}/links/bandwidth/{u}/{v}")
```
- Returns the bandwidth between nodes *u* and *v*.

---

### **🔹 Karan Mansuria**
```java
@GetMapping("/{id}/links/{u}")
```
- Retrieves all existing links from node *u*.

```java
@DeleteMapping("/{id}/links/{u}/{v}")
```
- Deletes the link between nodes *u* and *v* (tested via Postman).

---

### **🔹 Vedant Savani**
```java
@GetMapping("/{id}/network/path/{u}/{v}")
```
- Returns the **actual network path** between nodes *u* and *v*.

```java
@GetMapping("/{id}/network/path/cost/{u}/{v}")
```
- Returns the **path cost** (mathematical distance) between *u* and *v*.

---

### **🔹 Tanuj Shah**
```java
@GetMapping("/{id}/network/distance/{u}")
```
- Retrieves the **distance from node *u*** to all other nodes in the network.

```java
@GetMapping("/{id}/network/fullpathDetails/{u}/{v}")
```
- Returns **detailed path information** between nodes *u* and *v*.

---

## 🧩 Additional Information

All remaining API endpoints and functionalities were **jointly implemented** by all team members, ensuring equal contribution and collaborative effort across the project.

**Development Notes & Guidelines**
- Coding style: follow the existing style in the project. Keep changes small and focused.
- Tests: add unit tests under `routingSimulator/src/test/java`.
- Running the app: use `mvn spring-boot:run` for development server.
- Data files: `dns_records.txt` is consulted relative to working directory. Keep a copy in the project root for local runs.

**Troubleshooting**
- If DNS resolution inside the simulator fails, ensure `dns_records.txt` contains an entry like `example.com,192.168.1.10` and that the referenced IP corresponds to a registered `PublicServer` in the `GlobeManager`.
- If ports don't show up for a `Device`, make sure you added ports with `device.addPort(port)` before adding the device to its `Manager`.
