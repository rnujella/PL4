import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash(state, label);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof StateLabelPair)) return false;
            StateLabelPair other = (StateLabelPair) o;
            return state == other.state && label == other.label;
        }
    }

    HashSet<Integer> start_states = new HashSet<>();
    HashSet<Integer> accept_states = new HashSet<>();
    HashSet<Integer> current_states = new HashSet<>();
    HashMap<StateLabelPair, HashSet<Integer>> transitions = new HashMap<>();

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        if (is_start) start_states.add(s);
        if (is_accept) accept_states.add(s);
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair pair = new StateLabelPair(s_initial, label);
        transitions.computeIfAbsent(pair, k -> new HashSet<>()).add(s_final);
    }

    @Override
    public void reset() {
        current_states.clear();
        current_states.addAll(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> new_states = new HashSet<>();
        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, input);
            if (transitions.containsKey(pair)) {
                new_states.addAll(transitions.get(pair));
            }
        }
        current_states = new_states;
    }

    @Override
    public boolean accepts() {
        for (int state : current_states) {
            if (accept_states.contains(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, label);
            if (transitions.containsKey(pair)) {
                return true;
            }
        }
        return false;
    }
}
