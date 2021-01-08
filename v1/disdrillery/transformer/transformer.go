package transformer

type Transformer interface {
	GetName() string
	// Operational level defines how and where the transformer is called within the mining process
	GetOperationalLevel() string
	Export()
}
