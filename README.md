# Experiment

Java library for a Lean Startup style A/B - Switch Test experiment framework.

* Supports A/B Testing or Multi-outcome testing.
* Includes persistence of experiments.
* Relational database supports migrations via Liquibase.
* Supports getting a random outcome.
* Well encapsulated API.
* Jar artifact to plug in to an existing java app.
* Web app exposing RESTful web services for interacting with an experiment on a different server.
* Examples

## App

### Usage

#### Setting up an experiment

The following example illustrates an A/B test where different home pages are dispersed based on the server host.

<pre>
	...
	Option home1 = new Option("home1.html");
	Option home2 = new Option("home2.html");
	AorBExperiment homePageExperiment = new AorBExperiment(new ExperimentName("Home Page"), home1, home2);
	Subject server1 = new Subject("10.1.1.1");
	Subject server2 = new Subject("10.1.1.2");
	
	homePageExperiment.specifySubjectOutcome(server1, home1);
	homePageExperiment.specifySubjectOutcome(server2, home2);
	
	experimentRepository.store(homePageExperiment);
	...
</pre>

#### Exercising the experiment

<pre>
	// in home page code...
	Experiment homePageExperiment = experimentRepository.findBy(new ExperimentName("Home Page"));
	
	// server IP address in "serverIp" variable
	Option homePageFile = homePageExperiment.evaluateOutcomeFor(new Subject(serverIp));
	
	render(homePageFile.val());
	...
</pre>

### Building

(coming soon)

## Web app

### Usage

(coming soon)
