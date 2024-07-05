package frc.robot.modes;

import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.Superstructure.*;

public class TeleopModeManager extends BaseTeleopModeManager {
    private static TeleopModeManager INSTANCE = null;
    public static TeleopModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeleopModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        super.updateSwerve();
        super.updateLED();

        Superstate desiredState;

        // Scoring a note
        if (controls.SCORE.isTrue()) { // Score note
            desiredState = Superstate.kScore;
        } else if (controls.PASS.isTrue()) { // Pass note
            Superstructure.getInstance().setScoreState(ScoreState.kPass);
            desiredState = Superstate.kScore;
        } 

        // Priming for scoring
        else if (controls.PRIME.isTrue()) { // Ranged shot prime
            desiredState = Superstate.kPrime;
            Superstructure.getInstance().setScoreState(ScoreState.kRanged);
        } else if (controls.AMP_POSITION.isTrue()) { // Amp shot prime
            desiredState = Superstate.kPrime;
            Superstructure.getInstance().setScoreState(ScoreState.kAmp);
        } else if (controls.PODIUM_SHOT.isTrue()) { // Podium shot prime
            desiredState = Superstate.kPrime;
            Superstructure.getInstance().setScoreState(ScoreState.kPodium);
        } else if (controls.SUBWOOFER_SHOT.isTrue()) { // Subwoofer shot prime
            desiredState = Superstate.kPrime;
            Superstructure.getInstance().setScoreState(ScoreState.kSubwoofer);
        }
        
        // Handling of note
        else if (controls.INTAKE.isTrue()) { // Intake
            desiredState = Superstate.kIntake;
        } else if (controls.OUTAKE.isTrue()) { // Outake
            desiredState = Superstate.kOutake;
        }  
        
        // Elevator specific
        else if (controls.CLIMB_POSITION.isTrue()) { // Climb position
            desiredState = Superstate.kClimb;
        } else if (controls.CLIMB_RAISE.isTrue()) { // Raise climber
            desiredState = Superstate.kDefault;
            Superstructure.getInstance().raiseClimber();
        } else if (controls.CLIMB_LOWER.isTrue()) { // Lower climber
            desiredState = Superstate.kDefault;
            Superstructure.getInstance().lowerClimber();
        }

        // General
        else if (controls.STOW.isTrue() || controls.SCORE.isFallingEdge()) { // Stow
            desiredState = Superstate.kStow;
            Superstructure.getInstance().setScoreState(ScoreState.kNone);
        }

        // Hold states
        else if (Superstructure.getInstance().getState() == Superstate.kPrime) { // Priming
            desiredState = Superstate.kPrime;
        } else if (Superstructure.getInstance().getState() == Superstate.kClimb) { // Climbing
            desiredState = Superstate.kClimb;
        } else {
            desiredState = Superstate.kDefault;
        }

        Superstructure.getInstance().setSuperState(desiredState);
    }
}