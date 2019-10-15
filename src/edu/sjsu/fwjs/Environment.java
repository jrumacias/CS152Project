package edu.sjsu.fwjs;

import java.util.Map;
import java.util.HashMap;

public class Environment {
    private Map<String,Value> env = new HashMap<String,Value>();
    private Environment outerEnv;

    /**
     * Constructor for global environment
     */
    public Environment() {}

    /**
     * Constructor for local environment of a function
     */
    public Environment(Environment outerEnv) {
        this.outerEnv = outerEnv;
    }

    /**
     * Handles the logic of resolving a variable.
     * If the variable name is in the current scope, it is returned.
     * Otherwise, search for the variable in the outer scope.
     * If we are at the outermost scope (AKA the global scope)
     * null is returned (similar to how JS returns undefined.
     */
    public Value resolveVar(String varName) {
        Environment currentEnv = this;
        Value currentVar = currentEnv.getVar(varName);
        while (currentVar == null && currentEnv.getOuterEnv() != null) {
            currentEnv = currentEnv.getOuterEnv();
            currentVar = currentEnv.getVar(varName);
        }
        if (currentVar == null) {
            return new NullVal();
        }

        return currentVar;
    }

    /**
     * Used for updating existing variables.
     * If a variable has not been defined previously in the current scope,
     * or any of the function's outer scopes, the var is stored in the global scope.
     */
    public void updateVar(String key, Value v) {
        Environment currentEnv = this;
        // Check if the variable not found and not at global scope.
        while(currentEnv.getVar(key) == null &&
                currentEnv.getOuterEnv() != null) {
            // Go to next scope.
            currentEnv = currentEnv.getOuterEnv();
        }
        // Variable found or at global scope, set variable in env.
        currentEnv.setVar(key, v);
    }

    /**
     * Creates a new variable in the local scope.
     * If the variable has been defined in the current scope previously,
     * a RuntimeException is thrown.
     */
    public void createVar(String key, Value v) {
        if (env.get(key) == null) {
            env.put(key, v);
        }
        else throw new RuntimeException("Variable already defined");
    }
    /**
     * Gets variable from env HashMap.
     * @param varName variable to get.
     * @return variable value or null if it does not exist.
     */
    public Value getVar(String varName) {
        return env.get(varName);
    }

    /**
     * Sets a variable with a given key in the env HashMap.
     * @param key variable reference.
     * @param v variable value.
     */
    public void setVar(String key, Value v) {
        env.put(key, v);
    }

    /**
     * Gets the Environment outside of this Environment.
     * @return the outer Environment or null if there is none.
     */
    public Environment getOuterEnv() {
        return outerEnv;
    }
}