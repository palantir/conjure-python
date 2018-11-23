from conjure_client import Service, ConjureEncoder, ConjureDecoder

class SimpleNestedService2(Service):

    def test_endpoint(self, string):
        # type: (str) -> str

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str)

