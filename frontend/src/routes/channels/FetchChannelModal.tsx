import {Button, Form, InputGroup, Modal} from "react-bootstrap";
import {ChangeEvent, FormEvent, useState} from "react";
import {ArrowDownRightCircleFill} from "react-bootstrap-icons";

const handleRegex = /^@([A-Za-z0-9_\-.]{3,30})$/;
const urlRegex = /^(?:https?:\/\/)?(?:www\.|m\.)?youtube\.com\/@([A-Za-z0-9_\-.]{3,30})/;

type FetchChannelModalProps = {
    show: boolean,
    setShow: (show: boolean) => void,
    onSubmit: (channelHandle: string) => void,
}

export const FetchChannelModal = (props: FetchChannelModalProps) => {

    const [channelHandle, setChannelHandle] = useState("");

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        event.preventDefault();

        const value = event.target.value;
        const handleMatch = value.match(handleRegex)
        const urlMatch = value.match(urlRegex);

        if (handleMatch) {
            setChannelHandle(handleMatch[1]);
        } else if (urlMatch) {
            setChannelHandle(urlMatch[1]);
        } else {
            setChannelHandle(value);
        }
    };

    const handleShow = () => {
        setChannelHandle("");
    }

    const handleHide = () => {
        props.setShow(false);
    }

    const handleFetch = () => {
        props.onSubmit(channelHandle);
        props.setShow(false);
    }

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        handleFetch();
    }

    return (
        <Modal show={props.show} onShow={handleShow} onHide={handleHide}>
            <Modal.Header closeButton>
                <Modal.Title>Fetch channel</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group>
                        <Form.Label>
                            YouTube channel handle or URL
                        </Form.Label>
                        <InputGroup>
                            <InputGroup.Text>@</InputGroup.Text>
                            <Form.Control
                                autoFocus={true}
                                value={channelHandle}
                                onChange={handleChange}
                            />
                        </InputGroup>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button
                    variant={"outline-secondary"}
                    onClick={handleHide}
                >
                    Cancel
                </Button>
                <Button
                    variant={"primary"}
                    className={"align-items-center d-flex"}
                    onClick={handleFetch}
                >
                    Fetch
                    <ArrowDownRightCircleFill className="ms-2" />
                </Button>
            </Modal.Footer>
        </Modal>
    );
}
