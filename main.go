// var 3 (CallCenter)
package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Customer struct {
	name           string
	requeueTimeout int
}

type Operator struct {
	name string
}

type CallCenter struct {
	customers     []*Customer
	operators     []*Operator
	customerQueue chan *Customer
	mu            sync.Mutex
}

func NewCustomer(name string) *Customer {
	return &Customer{
		name:           name,
		requeueTimeout: rand.Intn(10) + 1,
	}
}

func NewOperator(name string) *Operator {
	return &Operator{
		name: name,
	}
}

func NewCallCenter(operatorCount int) *CallCenter {
	callCenter := &CallCenter{
		customerQueue: make(chan *Customer, 10),
	}

	for i := 1; i <= operatorCount; i++ {
		callCenter.operators = append(callCenter.operators, NewOperator(fmt.Sprintf("Operator %d", i)))
	}

	return callCenter
}

func (c *CallCenter) AddCustomer(customer *Customer) {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.customers = append(c.customers, customer)
}

func (c *CallCenter) AssistCustomer(operator *Operator) {
	c.mu.Lock()
	defer c.mu.Unlock()

	if len(c.customers) == 0 {
		return
	}

	customer := c.customers[0]
	c.customers = c.customers[1:]

	fmt.Printf("%s is assisting customer: %s\n", operator.name, customer.name)

	if customer.requeueTimeout > 0 {
		time.Sleep(time.Duration(customer.requeueTimeout) * time.Second)
		fmt.Printf("%s has left the queue and will call back in %d seconds\n", customer.name, customer.requeueTimeout)
		c.AddCustomer(customer)
	}
}

func main() {
	rand.Seed(time.Now().UnixNano())

	callCenter := NewCallCenter(3) // 3 operators

	for i := 1; i <= 10; i++ {
		customer := NewCustomer(fmt.Sprintf("Customer %d", i))
		callCenter.AddCustomer(customer)
	}

	for _, operator := range callCenter.operators {
		go func(op *Operator) {
			for {
				callCenter.AssistCustomer(op)
			}
		}(operator)
	}

	for {
		time.Sleep(1 * time.Second)
	}
}
