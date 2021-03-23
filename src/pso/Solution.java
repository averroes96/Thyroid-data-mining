package pso;

public abstract class Solution<S, T> {

    private Problem<T> problem ;
    private S solution ;

    public Solution(S solution , Problem<T> problem ) {
        setValue(solution);
        setProblem(problem);
    }

    abstract public Solution<S, T> getEmptySolution() ;

    abstract public Solution<S, T> randomSolution();

    abstract public boolean isSolution();

    abstract public int Fitness() ;

    abstract public int Distance(Solution<S, T> s) ;

    abstract public void update(int position)  ;

    abstract public Solution<S, T> copy() ;

    public S getValue(){
        return solution ;
    }

    public void setValue(S solution){
        this.solution =solution ;
    }

    public Problem<T> getProblem() {
        return problem;
    }

    public void setProblem(Problem<T> problem) {
        this.problem = problem;
    }

}