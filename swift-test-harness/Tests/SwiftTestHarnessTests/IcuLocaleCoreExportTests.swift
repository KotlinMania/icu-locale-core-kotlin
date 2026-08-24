import Testing
import IcuLocaleCore

@Suite("IcuLocaleCore Export Tests")
struct IcuLocaleCoreExportTests {
    @Test("Swift module loads and is callable")
    func swiftModuleLoads() throws {
        #expect(true, "IcuLocaleCore swift module imported cleanly")
    }
}
