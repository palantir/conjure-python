from conjure import ConjureEnumType

class EnumExample(ConjureEnumType):
    """This enumerates the numbers 1:2."""

    ONE = 'ONE'
    '''ONE'''
    TWO = 'TWO'
    '''TWO'''
    UNKNOWN = 'UNKNOWN'
    '''UNKNOWN'''

    def __reduce_ex__(self, proto):
        return self.__class__, (self.name,)

