from ..product import StringExample
from ..product_datasets import BackingFileSystem
from conjure_python_client import ConjureDecoder
from conjure_python_client import ConjureEncoder
from conjure_python_client import Service

class ImportService(Service):

    def test_endpoint(self, imported_string):
        # type: (StringExample.StringExample) -> BackingFileSystem.BackingFileSystem

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(imported_string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), BackingFileSystem.BackingFileSystem)

