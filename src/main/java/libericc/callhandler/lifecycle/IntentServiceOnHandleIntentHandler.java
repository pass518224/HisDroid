package libericc.callhandler.lifecycle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import heros.EdgeFunction;
import heros.FlowFunction;
import heros.edgefunc.EdgeIdentity;
import heros.flowfunc.KillAll;
import libericc.callhandler.CallHandler;
import libericc.edgefunc.GetIntentEdge;
import libericc.flowfunc.KillAllExceptStaticField;
import libericc.value.GeneralValue;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;

public class IntentServiceOnHandleIntentHandler extends CallHandler {

	@Override
	public Set<MethodSig> getTargets() {
		Set<MethodSig> targets = new HashSet<MethodSig>();
		targets.add(new MethodSig("android.app.IntentService", "void onHandleIntent(android.content.Intent)"));
		return targets;
	}

	public FlowFunction<Value> getCallFlowFunction(Unit callStmt, SootMethod destinationMethod, final Value zeroValue){
		final List<Value> paramLocals = new ArrayList<Value>();
		final StaticFieldRef iccNo = Jimple.v().newStaticFieldRef(Scene.v().getSootClass("DummyMain").getFieldByName("iccNo").makeRef());
		for (int i=0;i<destinationMethod.getParameterCount();i++) {
			paramLocals.add(destinationMethod.getActiveBody().getParameterLocal(i));
		}
		
		return new FlowFunction<Value>() {
			@Override
			public Set<Value> computeTargets(Value source) {
				Set<Value> ret = new HashSet<Value>();
				if (source instanceof StaticFieldRef) {
					ret.add(source);
				}
				if (source.equals(iccNo)){
					ret.add(paramLocals.get(0));
				}
				return ret;
			}
		};
	}

	public EdgeFunction<GeneralValue> getCallEdgeFunction(Unit callStmt, Value srcNode, SootMethod destinationMethod, Value destNode, Value zeroValue){
		final StaticFieldRef iccNo = Jimple.v().newStaticFieldRef(Scene.v().getSootClass("DummyMain").getFieldByName("iccNo").makeRef());
		if (srcNode.equivTo(iccNo) && destinationMethod.getActiveBody().getParameterLocals().indexOf(destNode)==0) {
			return new GetIntentEdge(libericc.Config.getIccLogs());
		}
		return EdgeIdentity.v();
	}

	@Override
	public FlowFunction<Value> getReturnFlowFunction(Unit callSite, SootMethod calleeMethod, Unit exitStmt, Unit returnSite, Value zeroValue){
		return KillAllExceptStaticField.v();
	}
	
	@Override
	public FlowFunction<Value> getCallToReturnFlowFunction(Unit callSite, Unit returnSite, Value zeroValue) {
		return KillAll.v();
	}

	@Override
	public EdgeFunction<GeneralValue> getCallToReturnEdgeFunction(Unit callSite, Value callNode, Unit returnSite,
			Value returnSideNode, Value zeroValue) {
		return EdgeIdentity.v();
	}

}
