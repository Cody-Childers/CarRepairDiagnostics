package com.ubiquisoft.evaluation;

import com.ubiquisoft.evaluation.domain.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Map;

import static java.lang.System.exit;

public class CarDiagnosticEngine {

	public void executeDiagnostics(Car car) {
		/*
		 * Implement basic diagnostics and print results to console.
		 *
		 * The purpose of this method is to find any problems with a car's data or parts.
		 *
		 * Diagnostic Steps:
		 *      First   - Validate the 3 data fields are present, if one or more are
		 *                then print the missing fields to the console
		 *                in a similar manner to how the provided methods do.
		 *
		 *      Second  - Validate that no parts are missing using the 'getMissingPartsMap' method in the Car class,
		 *                if one or more are then run each missing part and its count through the provided missing part method.
		 *
		 *      Third   - Validate that all parts are in working condition, if any are not
		 *                then run each non-working part through the provided damaged part method.
		 *
		 *      Fourth  - If validation succeeds for the previous steps then print something to the console informing the user as such.
		 * A damaged part is one that has any condition other than NEW, GOOD, or WORN.
		 *
		 * Important:
		 *      If any validation fails, complete whatever step you are actively on and end diagnostics early.
		 *
		 * Treat the console as information being read by a user of this application. Attempts should be made to ensure
		 * console output is as least as informative as the provided methods.
		 */

		//Step 1
		DiagnosticStepResult carManufactureDetailsStepResult = verifyCarManufactureDetails(car);
		printDiagnosticStepResultOutput(carManufactureDetailsStepResult);

		//Step 2
		DiagnosticStepResult partsAssemblyStepResult = validatePartsAssembly(car);
		printDiagnosticStepResultOutput(partsAssemblyStepResult);

		//Step 3
		DiagnosticStepResult carPartsWorkingConditionResult = verifyCarPartsWorkingConditionStatus(car);
		printDiagnosticStepResultOutput(carPartsWorkingConditionResult);

		//We will only see this output if all steps passed successfully.
		DiagnosticStepResult overallDiagnosticResult = new DiagnosticStepResult();
		boolean overallDiagnosticSuccessStatus =
				carManufactureDetailsStepResult.isDiagnosticStepSuccessful()
				&& partsAssemblyStepResult.isDiagnosticStepSuccessful()
				&& carPartsWorkingConditionResult.isDiagnosticStepSuccessful();

		overallDiagnosticResult.setDiagnosticStepSuccessful(overallDiagnosticSuccessStatus);

		if(overallDiagnosticSuccessStatus) {
			overallDiagnosticResult.setDiagnosticStepMessage("All diagnostic steps have passed successfully.");
		}

		printDiagnosticStepResultOutput(overallDiagnosticResult);
	}

	private void printDiagnosticStepResultOutput(DiagnosticStepResult diagnosticStepResult) {

		if(diagnosticStepResult.getDiagnosticStepMessage() != null) {
			System.out.println(diagnosticStepResult.getDiagnosticStepMessage());
			System.out.println("");
		}

		if(!diagnosticStepResult.isDiagnosticStepSuccessful()) {
			exit(1);
		}
	}

	private DiagnosticStepResult verifyCarManufactureDetails(Car carToVerify) {

		DiagnosticStepResult diagnosticStepResult = new DiagnosticStepResult();
		diagnosticStepResult.setDiagnosticStepSuccessful(false);

		if(carToVerify != null) {
			boolean isMakeValid = carToVerify.getMake() != null;
			boolean isModelValid = carToVerify.getModel() != null;
			boolean isYearValid = carToVerify.getYear() != null;

			diagnosticStepResult.setDiagnosticStepSuccessful(isMakeValid && isModelValid && isYearValid);

			if(diagnosticStepResult.isDiagnosticStepSuccessful()) {
				final String outputMessage =
						String.format("Performing Diagnostics on: %s %s %s",
									  carToVerify.getYear(),
									  carToVerify.getMake(),
									  carToVerify.getModel());

				diagnosticStepResult.setDiagnosticStepMessage(outputMessage);
			} else {

				StringBuilder errorMessageStringBuilder =
						new StringBuilder("Please provide the following manufacture details: ");

				if(!isYearValid) {
					errorMessageStringBuilder.append("Year ");
				}

				if(!isMakeValid) {
					errorMessageStringBuilder.append("Make ");
				}

				if(!isModelValid) {
					errorMessageStringBuilder.append("Model ");
				}

				diagnosticStepResult.setDiagnosticStepMessage(errorMessageStringBuilder.toString());
			}
		}

		return diagnosticStepResult;
	}

	private DiagnosticStepResult validatePartsAssembly(Car carToVerify) {

		DiagnosticStepResult partsAssemblyStepResult = new DiagnosticStepResult();
		boolean isPartsAssemblyComplete;

		if(carToVerify != null) {
			Map<PartType, Integer> missingPartsMap = carToVerify.getMissingPartsMap();

			if(missingPartsMap != null
				&& !missingPartsMap.isEmpty()) {

				boolean tireCountVerified = verifyRequiredPartCount(missingPartsMap, PartType.TIRE, 4);
				boolean engineCountVerified = verifyRequiredPartCount(missingPartsMap, PartType.ENGINE, 1);
				boolean electricalCountVerified = verifyRequiredPartCount(missingPartsMap, PartType.ELECTRICAL, 1);
				boolean oilFilterCountVerified = verifyRequiredPartCount(missingPartsMap, PartType.OIL_FILTER, 1);
				boolean fuelFilterCountVerified = verifyRequiredPartCount(missingPartsMap, PartType.FUEL_FILTER, 1);

				isPartsAssemblyComplete =
						tireCountVerified
						 && engineCountVerified
						 && electricalCountVerified
						 && oilFilterCountVerified
						 && fuelFilterCountVerified;

				partsAssemblyStepResult.setDiagnosticStepSuccessful(isPartsAssemblyComplete);
			} else {
				//We have no missing parts, so everything is installed.
				partsAssemblyStepResult.setDiagnosticStepSuccessful(true);
				partsAssemblyStepResult.setDiagnosticStepMessage("All required parts are installed.");
			}
		}

		return partsAssemblyStepResult;
	}

	private DiagnosticStepResult verifyCarPartsWorkingConditionStatus(Car carToVerify) {

		DiagnosticStepResult diagnosticStepResult = new DiagnosticStepResult();
		diagnosticStepResult.setDiagnosticStepSuccessful(true);

		if(carToVerify != null) {

			StringBuilder outputMessageStringBuilder = new StringBuilder();
			for(Part currentPart : carToVerify.getParts()) {

				if(!currentPart.isInWorkingCondition()) {
					printDamagedPart(currentPart.getType(), currentPart.getCondition());
					diagnosticStepResult.setDiagnosticStepSuccessful(false);
				}
			}

			if(diagnosticStepResult.isDiagnosticStepSuccessful()) {
				outputMessageStringBuilder.append("All parts are in working condition.");
			}

			diagnosticStepResult.setDiagnosticStepMessage(outputMessageStringBuilder.toString());
		}

		return diagnosticStepResult;
	}

	private boolean verifyRequiredPartCount(Map<PartType, Integer> missingPartsMap,
		                                    PartType partType,
		                                    int requiredQuantity) {
		boolean partCountIsVerified = false;

		if(missingPartsMap.containsKey(partType)) {
			int partCount = missingPartsMap.get(partType);

			if (partCount < requiredQuantity) {
				printMissingPart(partType, requiredQuantity - partCount);
			} else {
				partCountIsVerified = true;
			}
		}

		return partCountIsVerified;
	}

	private void printMissingPart(PartType partType, Integer count) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (count == null || count <= 0) throw new IllegalArgumentException("Count must be greater than 0");

		System.out.println(String.format("Missing Part(s) Detected: %s - Count: %s", partType, count));
	}

	private void printDamagedPart(PartType partType, ConditionType condition) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (condition == null) throw new IllegalArgumentException("ConditionType must not be null");

		System.out.println(String.format("Damaged Part Detected: %s - Condition: %s", partType, condition));
	}

	public static void main(String[] args) throws JAXBException {
		// Load classpath resource
		InputStream xml = ClassLoader.getSystemResourceAsStream("SampleCar.xml");
		//InputStream xml = ClassLoader.getSystemResourceAsStream("DamagedCar.xml");
		//InputStream xml = ClassLoader.getSystemResourceAsStream("MissingPartsCar.xml");
		//InputStream xml = ClassLoader.getSystemResourceAsStream("PerfectCar.xml");

		// Verify resource was loaded properly
		if (xml == null) {
			System.err.println("An error occurred attempting to load SampleCar.xml");

			exit(1);
		}

		// Build JAXBContext for converting XML into an Object
		JAXBContext context = JAXBContext.newInstance(Car.class, Part.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Car car = (Car) unmarshaller.unmarshal(xml);

		// Build new Diagnostics Engine and execute on deserialized car object.

		CarDiagnosticEngine diagnosticEngine = new CarDiagnosticEngine();

		diagnosticEngine.executeDiagnostics(car);

	}

}
