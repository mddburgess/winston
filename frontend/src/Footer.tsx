import { DateTime } from "luxon";

export const Footer = () => (
    <footer className={"mt-auto"}>
        <p
            className={
                "bg-body-tertiary border-top mb-0 mt-5 py-4 text-body-secondary text-center"
            }
        >
            &copy; {DateTime.now().year} Metrical Sky
        </p>
    </footer>
);
