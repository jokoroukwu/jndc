package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus;

public interface DiagnosticStatusAcceptor<V> {

    V withDiagnosticStatus(DiagnosticStatus diagnosticStatus);
}
