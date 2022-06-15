package cortex.jobmaster

import java.util.UUID
import scopt.OptionParser

trait CliParamsParser {

  def serviceName: String
  def tasksVersion: String

  case class JobMasterParams(
      // common options
      mode:        String  = "",
      debug:       Boolean = true,
      mesosMaster: String  = "",
      // jobs options
      jobType: String = "",
      // orion-ipc service options
      jobId: String = "",
      // absolute path of file containing baile's protobuf message
      filePath: String = "",
      // version of `cortex-job-master-tasks` docker image
      version: String = "latest"
  )

  protected def parser: OptionParser[JobMasterParams] = new OptionParser[JobMasterParams]("job-master") {
    head(serviceName, tasksVersion)
    help("help").text("Show help info")

    cmd("test").action((_, c) =>
      c.copy(mode = "test"))
      .text("Run predefined job by name").children(
        opt[String]('t', "type").required().action((x, c) =>
          c.copy(jobType = x)).text("'smoke' and 'gpu' are supported (for testing)"),

        opt[String]("mesos-master").required().action((x, c) =>
          c.copy(mesosMaster = x)).text("Address of mesos master")
      )

    cmd("service").action((_, c) =>
      c.copy(mode = "service")).text("Start cortex-job-master service (with Orion integration)").children(
      opt[String]("job-id").required().action((x, c) =>
        c.copy(jobId = x)).text("Job Id"),

      opt[String]("mesos-master").required().action((x, c) =>
        c.copy(mesosMaster = x)).text("Address of mesos master")
    )

    cmd("job").action((_, c) =>
      c.copy(mode = "job")).text("Start cortex-job-master service that reads incoming messages from file (with autogenerated id)").children(
      opt[String]("filepath").required().action((x, c) =>
        c.copy(filePath = x, jobId = UUID.randomUUID().toString)).text("Absolute path of file containing baile's protobuf message"),

      opt[String]("version").action((x, c) =>
        c.copy(version = x)).text("Version of `cortex-job-master-tasks` docker image. `latest` by default")
    )

    cmd("version").action((_, c) =>
      c.copy(mode = "version")).text("Show job-master version (commit hash)")

    opt[Unit]("debug").hidden().action((_, c) =>
      c.copy(debug = true)).text("Enable debugging")
  }

}