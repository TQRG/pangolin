package pt.up.fe.pangolin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import javassist.Modifier;
import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.events.VerboseEventListener;
import pt.up.fe.pangolin.core.instrumentation.FilterPass;
import pt.up.fe.pangolin.core.instrumentation.InstrumentationPass;
import pt.up.fe.pangolin.core.instrumentation.Pass;
import pt.up.fe.pangolin.core.instrumentation.StackSizePass;
import pt.up.fe.pangolin.core.instrumentation.TestFilterPass;
import pt.up.fe.pangolin.core.instrumentation.granularity.GranularityFactory.GranularityLevel;
import pt.up.fe.pangolin.core.instrumentation.matchers.BlackList;
import pt.up.fe.pangolin.core.instrumentation.matchers.DuplicateCollectorReferenceMatcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.FieldNameMatcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.Matcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.ModifierMatcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.OrMatcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.PrefixMatcher;
import pt.up.fe.pangolin.core.messaging.Client;

public class AgentConfigs {

	private int port = -1;
	private GranularityLevel granularityLevel = GranularityLevel.line;
	private List<String> prefixesToFilter = new ArrayList<String>();

	private List<Pass> passesToPrepend = new ArrayList<Pass>();

	public void setPort (int port) {
		this.port = port;
	}

	public int getPort () {
		return port;
	}

	public GranularityLevel getGranularityLevel() {
		return granularityLevel;
	}

	public void setGranularityLevel(GranularityLevel granularityLevel) {
		this.granularityLevel = granularityLevel;
	}

	public List<String> getPrefixesToFilter() {
		return prefixesToFilter;
	}

	public void setPrefixesToFilter(List<String> prefixesToFilter) {
		this.prefixesToFilter = prefixesToFilter;
	}

	public void addPrefixToFilter(String... prefixes) {
		for (String prefix : prefixes) {
			this.prefixesToFilter.add(prefix);
		}
	}

	public void prependPass(Pass pass) {
		passesToPrepend.add(pass);
	}

	@JSON(include = false)
	public List<Pass> getInstrumentationPasses() {
		List<Pass> instrumentationPasses = new ArrayList<Pass>();

		// Ignores classes in particular packages
		List<String> prefixes = new ArrayList<String> ();
		Collections.addAll(prefixes, "javax.", "java.", "sun.", "com.sun.", "pt.up.fe.pangolin.");
		prefixes.addAll(prefixesToFilter);

		PrefixMatcher pMatcher = new PrefixMatcher(prefixes);

		Matcher mMatcher = new OrMatcher(new ModifierMatcher(Modifier.NATIVE),
				new ModifierMatcher(Modifier.INTERFACE));

		Matcher alreadyInstrumented = new OrMatcher(new FieldNameMatcher(InstrumentationPass.HIT_VECTOR_NAME),
				new DuplicateCollectorReferenceMatcher());

		FilterPass fp = new FilterPass(new BlackList(mMatcher), 
				new BlackList(pMatcher),
				new BlackList(alreadyInstrumented));

		instrumentationPasses.addAll(passesToPrepend);
		instrumentationPasses.add(fp);
		instrumentationPasses.add(new TestFilterPass());
		instrumentationPasses.add(new InstrumentationPass(granularityLevel));
		instrumentationPasses.add(new StackSizePass());

		return instrumentationPasses;
	}

	@JSON(include = false)
	public EventListener getEventListener() {
		if (getPort() != -1) {
			return new Client(getPort());
		}
		else {
			return new VerboseEventListener();
		}
	}

	public String serialize () {
		return new JSONSerializer().exclude("*.class").deepSerialize(this);
	}

	public static AgentConfigs deserialize (String str) {
		try {
			return new JSONDeserializer<AgentConfigs> ().deserialize(str, AgentConfigs.class);
		}
		catch (Throwable t) {
			return null;
		}
	}
}
