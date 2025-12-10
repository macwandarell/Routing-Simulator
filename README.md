# Routing-Simulator

A small teaching and demonstration routing simulator written in Java (Spring Boot project layout).

This repository contains a lightweight routing simulator with modules for devices, routers,
DHCP, DNS server, and a few simple command utilities (ping, nslookup, nmap-like scanner).

## Project Goals
- Provide a simple simulation environment for networking concepts (IP addressing, DHCP, DNS, routing).
- Offer small command modules to exercise the simulated network.
- Serve as a learning codebase for students and contributors.

**Status:** Use the instructions below to build, run, and extend.

---
## Description

The **Routing Sandbox** project is a lightweight **network simulator** developed in **Java (Spring Boot)**.  
It provides a practical environment for simulating networking operations such as **ping**, **nmap**, and **nslookup**, along with building and visualizing **network topologies**.

### Purpose
To develop an educational and experimental routing simulator that helps users learn and test fundamental networking concepts and routing algorithms.

### Scope
The system supports:
- Running network commands (ping, nslookup, nmap).
- Building and managing network topologies with routers and devices.
- Simulating routing algorithms like Dijkstra’s shortest path.
- Managing DNS, DHCP, and IP allocations.
Data is stored using simple file handling (TXT, JSON).

### Intended Users
- **Educational institutions** for networking courses.
- **Network professionals** seeking a lightweight testing environment.
- **Developers** and **learners** exploring routing and topology simulation.

### System Overview
The simulator accepts API-based input, processes it through a layered **Model → Service → Controller** architecture,  
and stores persistent state in files (`dns_records.txt`, `network.json`, etc.).  
This makes the project modular, lightweight, and easily extensible.


## Table of Contents
- [Project Overview](#project-overview)
- [Architecture & Important Classes](#architecture--important-classes)
- [Build & Run](#build--run)
- [Testing](#testing)
- [Running the Included Nmap-style Command](#running-the-included-nmap-style-command)
- [Data Files](#data-files)
- [Contributors](#contributors)
- [API Endpoints](#api-endpoints)
- [Development Notes & Guidelines](#development-notes--guidelines)
- [Troubleshooting](#troubleshooting)
---

## Project Overview

The simulator models a simple network with managers (tenants), each managing devices and small services.
It includes the following subsystems:

- **DHCP:** Generates and assigns private IPv4 addresses.
- **DNS:** Small in-memory DNS backed by `dns_records.txt`.
- **Devices:** Models network devices exposing ports.
- **Commands:** CLI-like utilities implemented as Java classes (e.g., `NmapCommand`).

This code follows a Spring Boot project layout but can be executed as a standalone Java application.

---

## Architecture & Important Classes

- **`GlobeManager`** (`modules/manager/GlobeManager.java`): Top-level manager of the simulated network. Manages `Manager` instances, the network graph, and the `DNSServer`.
- **`Manager`** (`modules/manager/Manager.java`): Groups `Model` instances (devices, routers, DHCP, public servers) and assigns public IPs.
- **`Device`** (`modules/models/device/Device.java`): Represents a host with an IPv4 and a list of active ports.
- **`DNSServer`** (`modules/models/DnsServer/DNSServer.java`): Lightweight DNS implementation backed by a text file.
- **`NmapCommand`** (`modules/commands/NmapCommand.java`): TCP-based port scanner that works on real or simulated devices.

Refer to `routingSimulator/src/main/java/com/example/routingSimulator` for all modules (`network`, `models`, `service`, `controller`).

---

## Build & Run

### Prerequisites
- Java 11+ (JDK)
- Maven installed (`mvn` on PATH)

### Build
```bash
cd routingSimulator
mvn -DskipTests package
```

### Run (Spring Boot)
```bash
cd routingSimulator
mvn spring-boot:run
```

### Run (Standalone)
```bash
java -cp target/classes com.example.routingSimulator.Main
```

---

## Data Files

- **`dns_records.txt`** — Stores domain-to-IP mappings (e.g., `example.com,192.168.1.10`).
- **`data/services.txt`** — Optional service-to-port mapping used by `NmapCommand`.

---

## Contributors

All contributors made **equal and significant contributions** involving **design, implementation, and testing** of core modules — including Manager, Models, Network Graph, Commands, and Services.

| Contributor | Contributions |
|--------------|----------------|
| **Jos Samuel Biju** | Implemented the **N-map command**, developed `Device.java`, and handled **debugging and testing**. |
| **Macwan Darell** | Designed the **Abstract Model Class** and developed models like **Router**, **Public Server**, and **DHCP**. Implemented **IP logic**. |
| **Vedansh Patel** | Developed **connection link functionality** and integrated **command operations**. |
| **Vedant Savani** | Implemented **Network Topology** and integrated **Dijkstra’s Algorithm** for pathfinding. |
| **Karan Mansuria** | Implemented the **DNS Server**, **NS-Lookup command**, and **file management**. |
| **Tanuj Shah** | Implemented the **Ping command** and **Manager module**, and assisted in **debugging and testing**. |

---

## API Endpoints

### Macwan Darell
```java
@GetMapping("/{id}/managers")
```
Retrieves all managers within a specific sandbox.

```java
@GetMapping("/{id}/{mid}/devices")
```
Lists all devices managed by a specific manager.

---

### Jos Samuel Biju
```java
@GetMapping("/{id}/{deviceid}")
```
Lists all active ports for a specific device.

```java
@GetMapping("/{id}/network")
```
Returns the complete network graph.

---

### Vedansh Patel
```java
@GetMapping("/{id}/links/{u}/{v}")
```
Provides detailed information about the link between nodes *u* and *v*.

```java
@GetMapping("/{id}/links/bandwidth/{u}/{v}")
```
Returns the bandwidth between nodes *u* and *v*.

---

### Karan Mansuria
```java
@GetMapping("/{id}/links/{u}")
```
Retrieves all links originating from node *u*.

```java
@DeleteMapping("/{id}/links/{u}/{v}")
```
Deletes the link between nodes *u* and *v* (tested using Postman).

---

### Vedant Savani
```java
@GetMapping("/{id}/network/path/{u}/{v}")
```
Returns the **actual network path** between nodes *u* and *v*.

```java
@GetMapping("/{id}/network/path/cost/{u}/{v}")
```
Returns the **path cost** (mathematical distance) between *u* and *v*.

---

### Tanuj Shah
```java
@GetMapping("/{id}/network/distance/{u}")
```
Retrieves the **distance from node *u*** to all other nodes.

```java
@GetMapping("/{id}/network/fullpathDetails/{u}/{v}")
```
Returns **detailed path information** between nodes *u* and *v*.

---

## Development Notes & Guidelines
- Follow consistent coding style used in the project.
- Add unit tests under `routingSimulator/src/test/java`.
- Run the application with `mvn spring-boot:run` during development.
- Ensure `dns_records.txt` is located in the correct working directory.

---

## Troubleshooting

- **DNS Resolution Issues:** Verify that `dns_records.txt` includes valid domain-IP mappings. Example:  
  `example.com,192.168.1.10`  
  The IP should correspond to a registered `PublicServer` in the `GlobeManager`.

- **Missing Device Ports:** Ensure ports are added via `device.addPort(port)` before registering the device to a manager.

---

