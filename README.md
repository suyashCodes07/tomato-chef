# 🍅 Tomato Chef — AI-Powered Cooking Assistant

An AI-powered cooking assistant built using **Java, Spring Boot, Spring AI, and Google Gemini**. Tomato Chef helps users with cooking-related questions and recipe assistance through a simple web interface.

The project also demonstrates application observability through Spring Boot Actuator, Micrometer, Prometheus, and Grafana, along with Docker-based containerization and cloud deployment.

<p align="center">
  <a href="https://tomato-chef.onrender.com/">
    <strong>🚀 Try the Live Demo</strong>
  </a>
</p>

---

## 📸 Screenshots

### 1. Grafana Monitoring Dashboard

![Grafana Monitoring Dashboard](<Screenshot (4).png>)

### 2. Prometheus Metrics

![Prometheus Metrics](<Screenshot (5).png> )
---

## ✨ Features

- **AI-Powered Cooking Assistance:** Generates cooking-related responses using Google Gemini.
- **Spring AI Integration:** Connects the Spring Boot application to a generative AI model.
- **Web-Based Interface:** Provides a user-facing interface using Thymeleaf.
- **Application Health Monitoring:** Exposes health and application information through Spring Boot Actuator.
- **Custom Token Metrics:** Tracks Gemini input, output, and total token usage using Micrometer counters.
- **Metrics Collection:** Exposes application metrics in Prometheus-compatible format.
- **Grafana Dashboards:** Visualizes application and JVM metrics through Grafana.
- **Docker Support:** Uses a multi-stage Dockerfile to build and run the application.
- **Cloud Deployment:** Makes the application accessible through Render.

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Programming Language | Java 23 |
| Backend Framework | Spring Boot |
| AI Integration | Spring AI |
| AI Model Provider | Google Gemini |
| Frontend | Thymeleaf, HTML, CSS |
| Monitoring | Spring Boot Actuator, Micrometer |
| Metrics Collection | Prometheus |
| Visualization | Grafana |
| Containerization | Docker |
| Build Tool | Maven |
| Deployment | Render |

---

## 🏗️ System Architecture

```text
                 USER
                   |
                   v
           Thymeleaf Web UI
                   |
                   v
          Spring Boot Backend
                   |
                   v
               Spring AI
                   |
                   v
             Google Gemini
                   |
                   v
            AI-Generated Response


          OBSERVABILITY STACK

          Spring Boot Application
                   |
                   v
        Actuator + Micrometer
                   |
                   v
               Prometheus
                   |
                   v
                Grafana
```

### Architecture Overview

1. The user interacts with the application through the web interface.
2. The Spring Boot backend processes the request.
3. Spring AI integrates the backend with Google Gemini.
4. The generated response is returned to the user.
5. Spring Boot Actuator and Micrometer expose application metrics.
6. Prometheus scrapes the metrics endpoint.
7. Grafana visualizes the collected metrics through dashboards.

The monitoring stack is configured for local observability and can be extended for remote monitoring.

---

## ⚙️ Prerequisites

Ensure the following tools are available before running the project locally:

- Java Development Kit (JDK) 23
- Git
- Google Gemini API key
- Docker Desktop, if using the containerized setup
- Maven, optional if using the included Maven Wrapper

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/suyashCodes07/tomato.git
cd tomato
```

### 2. Configure the Gemini API Key

The application reads the API key from the `GEMINI_API_KEY` environment variable.

**Windows PowerShell**

```powershell
$env:GEMINI_API_KEY="your_gemini_api_key"
```

**Linux/macOS**

```bash
export GEMINI_API_KEY="your_gemini_api_key"
```

Replace `your_gemini_api_key` with your own API key.

**Security:** Never commit your API key to GitHub or include it in screenshots.

### 3. Run the Application

On Windows, use the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, build the application:

```powershell
.\mvnw.cmd clean package -DskipTests
```

Then run the generated JAR:

```bash
java -jar target/tomato-*.jar
```

The application uses port `8081` by default unless the `PORT` environment variable overrides it.

Open the following URL in your browser:

**http://localhost:8081**

---

## 🤖 AI Integration

Tomato Chef integrates Google Gemini through Spring AI.

The application uses Spring AI's chat capabilities to send cooking-related prompts to the configured Gemini model and return generated responses to the user.

The API key is configured through an environment variable rather than being hardcoded in the application source code.

### Custom Token Usage Metrics

The application defines Micrometer counters for:

- `tomato.gemini.input.tokens`
- `tomato.gemini.output.tokens`
- `tomato.gemini.total.tokens`

These counters provide visibility into token usage recorded by the application.

Micrometer's Prometheus registry typically exports these names with dots converted to underscores:

```text
tomato_gemini_input_tokens
tomato_gemini_output_tokens
tomato_gemini_total_tokens
```

> Token counters reflect what the application records. They should not be treated as independently verified billing totals.

---

## 📊 Application Monitoring

Tomato Chef uses Spring Boot Actuator and Micrometer to expose operational information and metrics.

### Available Actuator Endpoints

| Endpoint | Description |
|---|---|
| `/actuator/health` | Reports application health |
| `/actuator/info` | Displays configured application information |
| `/actuator/metrics` | Lists available application metrics |
| `/actuator/prometheus` | Exposes metrics for Prometheus scraping |

For a locally running application, visit:

```text
http://localhost:8081/actuator/health
http://localhost:8081/actuator/info
http://localhost:8081/actuator/metrics
http://localhost:8081/actuator/prometheus
```

The endpoints exposed by the application depend on the configured Actuator settings.

### Prometheus

Prometheus periodically scrapes the configured application metrics endpoint and stores the collected time-series data.

Example local target configuration:

```yaml
global:
  scrape_interval: 5s

scrape_configs:
  - job_name: "tomato-chef"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["host.docker.internal:8081"]
```

The target above is intended for a local Spring Boot application running on the host while Prometheus runs in Docker Desktop.

### Grafana

Grafana connects to Prometheus as a data source and visualizes the collected metrics.

Example local URLs:

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

Example Prometheus queries for Grafana panels:

**Application availability**

```promql
up{job="tomato-chef"}
```

**JVM heap memory usage in MiB**

```promql
sum(jvm_memory_used_bytes{area="heap"}) / 1024 / 1024
```

**Process CPU usage as a percentage**

```promql
process_cpu_usage * 100
```

**Total recorded Gemini input tokens**

```promql
tomato_gemini_input_tokens
```

**Total recorded Gemini output tokens**

```promql
tomato_gemini_output_tokens
```

**Total recorded Gemini tokens**

```promql
tomato_gemini_total_tokens
```

Metric availability depends on the metrics exported by the running application.

> **Monitoring note:** The example Prometheus configuration targets the local application. The local Grafana dashboard is separate from the public Render deployment and does not automatically monitor the live service.

---

## 🐳 Docker

The project includes a multi-stage Dockerfile. The build stage packages the application using Maven, while the runtime stage executes the generated JAR.

### Build the Docker Image

```bash
docker build -t tomato-chef .
```

### Run the Container

```bash
docker run --rm -p 8081:8081 -e PORT=8081 -e GEMINI_API_KEY=your_gemini_api_key tomato-chef
```

Open http://localhost:8081 to access the application.

The API key is passed through an environment variable. Use a secure secrets-management mechanism for shared or production environments.

---

## ☁️ Deployment

Tomato Chef is deployed on Render.

**Live application:** https://tomato-chef.onrender.com/

The deployment uses the configured `PORT` environment variable and requires `GEMINI_API_KEY` to be configured in the Render environment settings.

The application may take some time to respond after inactivity, depending on the hosting plan. AI request availability also depends on the configured Google Gemini model, API quota, and provider availability.

---

## 🔐 Configuration and Security

| Variable | Purpose |
|---|---|
| `GEMINI_API_KEY` | Authenticates requests to Google Gemini |
| `PORT` | Configures the server port; defaults to `8081` locally |

Recommended practices:

- Keep API keys out of source control.
- Store secrets in environment variables or a secrets manager.
- Avoid exposing sensitive Actuator information publicly.
- Apply appropriate authentication and authorization before exposing administrative or sensitive endpoints.

---

## 🧠 Key Learning Outcomes

This project provided practical experience with:

- Building a Java-based backend application using Spring Boot.
- Integrating a generative AI model using Spring AI.
- Creating a web interface using Thymeleaf.
- Configuring external API credentials securely.
- Exposing application metrics with Actuator and Micrometer.
- Defining custom metrics for AI token usage.
- Collecting metrics with Prometheus.
- Building Grafana dashboards for application observability.
- Containerizing a Spring Boot application with Docker.
- Deploying a Java application to a cloud hosting platform.

---

## 🔮 Future Enhancements

- Recommend recipes based on ingredients available to the user.
- Add cuisine, preparation-time, and dietary preference filters.
- Introduce better handling for API quota limits and provider errors.
- Add automated unit and integration tests.
- Expand dashboards with HTTP latency, request rates, and error rates.
- Add conversation history and personalized cooking preferences.

---

## 👨‍💻 Author

**Suyash Dwivedi**

- GitHub: [suyashCodes07](https://github.com/suyashCodes07)
- Live Demo: [Tomato Chef](https://tomato-chef.onrender.com/)

---

*Tomato Chef is a learning project exploring AI integration, Java backend development, containerization, and application observability.*
