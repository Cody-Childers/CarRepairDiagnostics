package com.ubiquisoft.evaluation.domain;

public class DiagnosticStepResult
{

	private boolean isDiagnosticStepSuccessful;
	private String diagnosticStepMessage;

/* --------------------------------------------------------------------------------------------------------------- */
/*  Getters and Setters *///region
/* --------------------------------------------------------------------------------------------------------------- */
	public boolean isDiagnosticStepSuccessful()
	{
		return isDiagnosticStepSuccessful;
	}

	public void setDiagnosticStepSuccessful(boolean diagnosticStepSuccessful)
	{
		isDiagnosticStepSuccessful = diagnosticStepSuccessful;
	}

	public String getDiagnosticStepMessage()
	{
		return diagnosticStepMessage;
	}

	public void setDiagnosticStepMessage(String diagnosticStepMessage)
	{
		this.diagnosticStepMessage = diagnosticStepMessage;
	}
/* --------------------------------------------------------------------------------------------------------------- */
/*  Getters and Setters *///region
/* --------------------------------------------------------------------------------------------------------------- */
}
